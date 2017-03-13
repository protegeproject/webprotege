package edu.stanford.bmir.protege.web.server.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.server.shortform.EscapingShortFormProvider;
import edu.stanford.bmir.protege.web.shared.BrowserTextProvider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 *
 */
@ProjectSingleton
public class RenderingManager implements BrowserTextProvider, HasGetFrameRendering, HasHtmlBrowserText {

    private final BidirectionalShortFormProvider shortFormProvider;

    // An immutable map of IRI to OWLEntity for built in entities.
    private final ImmutableMap<IRI, OWLEntity> builtInEntities;

    private final OntologyIRIShortFormProvider ontologyIRIShortFormProvider;

    private final EntityIRIChecker entityIRIChecker;

    private final DeprecatedEntityChecker deprecatedEntityChecker;

    private final HighlightedEntityChecker highlightEntityChecker;
    
    private final OWLOntology rootOntology;

    private final OWLDataFactory dataFactory;

    private final WebProtegeLogger logger;

    @Inject
    public RenderingManager(@RootOntology OWLOntology rootOnt,
                            OWLDataFactory dataFactory,
                            EntityIRIChecker entityIRIChecker,
                            DeprecatedEntityChecker deprecatedChecker,
                            BidirectionalShortFormProvider shortFormProvider,
                            OntologyIRIShortFormProvider ontologyIRIShortFormProvider,
                            HighlightedEntityChecker highlightedEntityChecker,
                            WebProtegeLogger logger) {
        this.rootOntology = rootOnt;
        this.dataFactory = dataFactory;
        this.shortFormProvider = shortFormProvider;
        this.ontologyIRIShortFormProvider = ontologyIRIShortFormProvider;
        this.logger = logger;

        ImmutableMap.Builder<IRI, OWLEntity> builtInEntities = ImmutableMap.builder();

        putEntity(dataFactory.getOWLThing(), builtInEntities);
        putEntity(dataFactory.getOWLNothing(), builtInEntities);
        putEntity(dataFactory.getOWLTopObjectProperty(), builtInEntities);
        putEntity(dataFactory.getOWLBottomObjectProperty(), builtInEntities);
        putEntity(dataFactory.getOWLTopDataProperty(), builtInEntities);
        putEntity(dataFactory.getOWLBottomDataProperty(), builtInEntities);
        for (IRI iri : OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS) {
            putEntity(dataFactory.getOWLAnnotationProperty(iri), builtInEntities);
        }
        for(DublinCoreVocabulary vocabulary : DublinCoreVocabulary.values()) {
            OWLAnnotationProperty prop = dataFactory.getOWLAnnotationProperty(vocabulary.getIRI());
            putEntity(prop, builtInEntities);
        }
        for (OWLAnnotationProperty prop : SKOSVocabulary.getAnnotationProperties(dataFactory)) {
            putEntity(prop, builtInEntities);
        }
        for (OWL2Datatype dt : OWL2Datatype.values()) {
            OWLDatatype datatype = dataFactory.getOWLDatatype(dt.getIRI());
            putEntity(datatype, builtInEntities);
        }
        this.builtInEntities = builtInEntities.build();

        this.entityIRIChecker = entityIRIChecker;

        this.deprecatedEntityChecker = deprecatedChecker;

        this.highlightEntityChecker = highlightedEntityChecker;
    }

    /**
     * Adds an {@link OWLEntity} to a map, so that they entity is keyed by its {@link IRI}.
     * @param entity The entity to add.  Not null.
     * @param map The map to add the entity to. Not null.
     */
    private static void putEntity(OWLEntity entity, ImmutableMap.Builder<IRI, OWLEntity> map) {
        map.put(entity.getIRI(), entity);
    }

    public BidirectionalShortFormProvider getShortFormProvider() {
        return shortFormProvider;
    }

    public OntologyIRIShortFormProvider getOntologyIRIShortFormProvider() {
        return ontologyIRIShortFormProvider;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////  Main public interface
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Gets the set of entities that match a given entity name.  The entity name should be a string corresponding to
     * an IRI.  However, this method will also work for browser text so long as the browser text was generated by this
     * rendering manager.
     * @param entityName The entity name.  Should be an IRI.
     * @return A set of entities that have the specified name (or browser text).  The set will be empty if no such
     * entities exist.
     */
    public Set<OWLEntity> getEntities(String entityName) {
        // Some UI components mix up the use of entity name and browser text (actually, it's not clear what should happen
        // because users presumably primarily edit browser text).  So, in order to have a belt
        // and braces approach to things, we check to see if the entity name supplied above is in fact some browser
        // text.  In some case, there may be a one-to-many mapping, but this is just tough luck - badly behaving UI
        // components get what they deserve!
        Set<OWLEntity> entities = shortFormProvider.getEntities(entityName);
        if (!entities.isEmpty()) {
            printBrowserTextReferenceWarningMessage(entityName);
            return entities;
        }
        // Not referring to browser text, or there is no browser text!

        IRI iri = IRI.create(entityName);
        OWLEntity builtInEntity = builtInEntities.get(iri);
        if (builtInEntity != null) {
            return Collections.singleton(builtInEntity);
        }
        Set<OWLEntity> entitiesInSig = rootOntology.getEntitiesInSignature(iri, true);
        if(!entitiesInSig.isEmpty()) {
            return entitiesInSig;
        }
        return Collections.emptySet();
    }





    @Override
    public Optional<String> getOWLEntityBrowserText(OWLEntity entity) {
        String shortForm = shortFormProvider.getShortForm(entity);
        if(shortForm == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(shortForm);
        }
    }

    /**
     * Gets all of the short forms (browser text) of all entities.
     * @return A set of short forms for all entities.
     */
    public Set<String> getShortForms() {
        return shortFormProvider.getShortForms();
    }

    /**
     * Gets the short for for the specified entity.
     * @param entity The entity.
     * @return The entity short form. Not null.
     */
    public String getShortForm(OWLEntity entity) {
        return shortFormProvider.getShortForm(entity);
    }

    /**
     * Gets the browser text for a given OWLObject.
     * @param object The object.
     * @return The browser text for the object.
     */
    public String getBrowserText(OWLObject object) {
        OWLObjectRenderer owlObjectRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();
        if (object instanceof OWLEntity || object instanceof IRI) {
            owlObjectRenderer.setShortFormProvider(shortFormProvider);
        }
        else {
            owlObjectRenderer.setShortFormProvider(new EscapingShortFormProvider(shortFormProvider));
        }
        return owlObjectRenderer.render(object);
    }

    @Override
    public String getFrameRendering(OWLObject subject) {
        if(!(subject instanceof OWLEntity)) {
            return "";
        }
        OWLEntity entity = (OWLEntity) subject;
        ManchesterSyntaxObjectRenderer objectRenderer = new ManchesterSyntaxObjectRenderer(shortFormProvider,
                entityIRIChecker,
                LiteralStyle.REGULAR,
                new DefaultHttpLinkRenderer(),
                new MarkdownLiteralRenderer());
        ManchesterSyntaxEntityFrameRenderer renderer = new ManchesterSyntaxEntityFrameRenderer(
                rootOntology,
                shortFormProvider, ontologyIRIShortFormProvider, objectRenderer,
                highlightEntityChecker, deprecatedEntityChecker, new DefaultItemStyleProvider(), NestedAnnotationStyle.COMPACT);
        StringBuilder builder = new StringBuilder();
        renderer.render(entity, builder);
        return builder.toString();
    }

    public String getHTMLBrowserText(OWLObject object) {
        return getHTMLBrowserText(object, entity -> false);
    }

    @Override
    public SafeHtml getHtmlBrowserText(OWLObject object) {
        return new SafeHtmlBuilder().appendHtmlConstant(getHTMLBrowserText(object)).toSafeHtml();
    }

    public String getHTMLBrowserText(OWLObject object, final Set<String> highlightedPhrases) {
        return getHTMLBrowserText(object, entity -> highlightedPhrases.contains(getShortForm(entity)));
    }

    public String getHTMLBrowserText(OWLObject object, HighlightedEntityChecker highlightChecker) {
        ManchesterSyntaxObjectRenderer renderer = new ManchesterSyntaxObjectRenderer(
                getShortFormProvider(),
                new EntityIRICheckerImpl(rootOntology),
                LiteralStyle.BRACKETED,
                new DefaultHttpLinkRenderer(),
                new MarkdownLiteralRenderer());
        return renderer.render(object, highlightChecker, deprecatedEntityChecker);
    }


    /**
     * Selects a single entity from a set of entities.  The selection procedure works by sorting the entities in the
     * set and selecting the first entity.  This makes the choice of entity more predictable over multiple calls for
     * a given set.
     * @param entities The entities.  MUST NOT BE EMPTY!
     * @return A selected entity that is contained in the supplied set of entities.
     * @throws IllegalArgumentException if the set of entities is empty.
     */
    public static OWLEntity selectEntity(Set<OWLEntity> entities) throws IllegalArgumentException {
        if (entities.isEmpty()) {
            throw new IllegalArgumentException("Set of entities must not be empty");
        }
        else if (entities.size() == 1) {
            return entities.iterator().next();
        }
        else {
            List<OWLEntity> entitiesList = new ArrayList<>();
            entitiesList.addAll(entities);
            Collections.sort(entitiesList);
            return entitiesList.get(0);
        }
    }

    public OWLEntityData getRendering(OWLEntity entity) {
        return DataFactory.getOWLEntityData(entity, getShortForm(entity));
    }

    public OWLClassData getRendering(OWLClass cls) {
        return new OWLClassData(cls, getShortForm(cls));
    }

    public OWLObjectPropertyData getRendering(OWLObjectProperty property) {
        return new OWLObjectPropertyData(property, getShortForm(property));
    }

    public OWLDataPropertyData getRendering(OWLDataProperty property) {
        return new OWLDataPropertyData(property, getShortForm(property));
    }

    public OWLAnnotationPropertyData getRendering(OWLAnnotationProperty property) {
        return new OWLAnnotationPropertyData(property, getShortForm(property));
    }

    public OWLNamedIndividualData getRendering(OWLNamedIndividual individual) {
        return new OWLNamedIndividualData(individual, getShortForm(individual));
    }

    public OWLDatatypeData getRendering(OWLDatatype datatype) {
        return new OWLDatatypeData(datatype, getShortForm(datatype));
    }
    
    
    
    public Map<OWLEntity, OWLEntityData> getRendering(Set<OWLEntity> entities) {
        final Map<OWLEntity, OWLEntityData> result = new HashMap<>();
        for(OWLEntity entity : entities) {
            entity.accept(new OWLEntityVisitor() {
                @Override
                public void visit(@Nonnull OWLClass owlClass) {
                    result.put(owlClass, getRendering(owlClass));
                }

                @Override
                public void visit(@Nonnull OWLObjectProperty property) {
                    result.put(property, getRendering(property));
                }

                @Override
                public void visit(@Nonnull OWLDataProperty property) {
                    result.put(property, getRendering(property));
                }

                @Override
                public void visit(@Nonnull OWLNamedIndividual individual) {
                    result.put(individual, getRendering(individual));
                }

                @Override
                public void visit(@Nonnull OWLDatatype owlDatatype) {
                    result.put(owlDatatype, getRendering(owlDatatype));
                }

                @Override
                public void visit(@Nonnull OWLAnnotationProperty property) {
                    result.put(property, getRendering(property));
                }
            });
        }
        return result;
    }

    public void dispose() {
    }


    private void printBrowserTextReferenceWarningMessage(String referenceName) {
        logger.info("Could not find entity by name \"%s\".  This name may be the browser text rather than an entity IRI.", referenceName);
    }

}

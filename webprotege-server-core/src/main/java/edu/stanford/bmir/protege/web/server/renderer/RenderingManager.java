package edu.stanford.bmir.protege.web.server.renderer;

import com.google.common.collect.ImmutableMap;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.render.*;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxOWLObjectRendererImpl;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
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
public class RenderingManager implements HasGetRendering, HasHtmlBrowserText {

    private final BidirectionalShortFormProvider shortFormProvider;

    // An immutable map of IRI to OWLEntity for built in entities.
    private final ImmutableMap<IRI, OWLEntity> builtInEntities;

    private final DeprecatedEntityChecker deprecatedEntityChecker;

    private final OWLOntology rootOntology;

    private final ManchesterSyntaxObjectRenderer renderer;

    private final OWLObjectRenderer owlObjectRenderer = new ManchesterOWLSyntaxOWLObjectRendererImpl();

    @Inject
    public RenderingManager(@RootOntology OWLOntology rootOnt,
                            OWLDataFactory dataFactory,
                            DeprecatedEntityChecker deprecatedChecker,
                            BidirectionalShortFormProvider shortFormProvider,
                            HighlightedEntityChecker highlightedEntityChecker) {
        this.rootOntology = rootOnt;
        this.shortFormProvider = shortFormProvider;
        this.renderer = new ManchesterSyntaxObjectRenderer(
                getShortFormProvider(),
                new EntityIRICheckerImpl(rootOntology),
                LiteralStyle.BRACKETED,
                new DefaultHttpLinkRenderer(),
                new MarkdownLiteralRenderer());

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

        this.deprecatedEntityChecker = deprecatedChecker;

        owlObjectRenderer.setShortFormProvider(shortFormProvider);
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

    /**
     * Gets the set of entities that match a given entity short form.
     * @param shortForm The entity short form.
     * @return A set of entities that have the specified short form.  The set will be empty if no such
     * entities with the specified short form exist.
     */
    @Deprecated
    public Set<OWLEntity> getEntities(String shortForm) {
        return shortFormProvider.getEntities(shortForm);
    }

    /**
     * Gets the short for for the specified entity.
     * @param entity The entity.
     * @return The entity short form. Not null.
     */
    @Nonnull
    public String getShortForm(OWLEntity entity) {
        return shortFormProvider.getShortForm(entity);
    }

    /**
     * Gets the browser text for a given OWLObject.
     * @param object The object.
     * @return The browser text for the object.
     */
    @Deprecated
    public String getBrowserText(OWLObject object) {
        return owlObjectRenderer.render(object);
    }

    private String getHTMLBrowserText(OWLObject object) {
        return getHTMLBrowserText(object, entity -> false);
    }

    @Override
    public SafeHtml getHtmlBrowserText(OWLObject object) {
        return new SafeHtmlBuilder().appendHtmlConstant(getHTMLBrowserText(object)).toSafeHtml();
    }

    public String getHTMLBrowserText(OWLObject object, final Set<String> highlightedPhrases) {
        return getHTMLBrowserText(object, entity -> highlightedPhrases.contains(getShortForm(entity)));
    }



    private String getHTMLBrowserText(OWLObject object, HighlightedEntityChecker highlightChecker) {
        return renderer.render(object, highlightChecker, deprecatedEntityChecker);
    }

    public OWLEntityData getRendering(OWLEntity entity) {
        return DataFactory.getOWLEntityData(entity, getShortForm(entity));
    }

    public OWLPrimitiveData getRendering(OWLAnnotationValue value) {
        if(value instanceof IRI) {
            return new IRIData((IRI) value);
        }
        else if(value instanceof OWLLiteral) {
            return new OWLLiteralData((OWLLiteral) value);
        }
        else {
            throw new RuntimeException("Unsupported");
        }
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

    public void dispose() {
    }
}

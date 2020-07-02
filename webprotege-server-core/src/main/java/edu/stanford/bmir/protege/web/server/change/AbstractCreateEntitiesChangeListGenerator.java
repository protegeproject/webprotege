package edu.stanford.bmir.protege.web.server.change;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.msg.MessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.EntityShortFormsParser;
import edu.stanford.bmir.protege.web.shared.entity.FreshEntityIri;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.Namespaces;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static org.semanticweb.owlapi.model.EntityType.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 * <p>
 *     A base class for generating changes associated with entity creation.  This class accepts a set of string representing
 *     the browser text of the entities to be created, and an (optional) "parent" entity.  The notion of a "parent" entity is dependent
 *     on the type of entities being created.  For example, if classes are being created then the parent entity would
 *     usually be a super class that the freshly created entities will be subclasses of.  If individuals are being created
 *     then the parent entity might be a class that the freshly created individuals are instances of.  Subclasses of this
 *     class defined the exact type of behaviour of the parent entity and the necessary changes that are needed to form
 *     associations with the parent entity in the ontology.
 * </p>
 */
public abstract class AbstractCreateEntitiesChangeListGenerator<E extends OWLEntity, P extends OWLEntity> implements ChangeListGenerator<Set<E>> {

    @Nonnull
    private final EntityType<E> entityType;

    @Nonnull
    private final String sourceText;

    @Nonnull
    private final String langTag;

    @Nonnull
    private final ImmutableSet<P> parents;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final MessageFormatter msg;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    private static Map<String, String> builtInPrefixes = new HashMap<>();

    static {
        builtInPrefixes.put("owl:", Namespaces.OWL.toString());
        builtInPrefixes.put("rdf:", Namespaces.RDF.toString());
        builtInPrefixes.put("rdfs:", Namespaces.RDFS.toString());
        builtInPrefixes.put("skos:", Namespaces.SKOS.toString());
        builtInPrefixes.put("swrl:", Namespaces.SWRL.toString());
        builtInPrefixes.put("swrlb:", Namespaces.SWRLB.toString());
        builtInPrefixes.put("xsd:", Namespaces.XSD.toString());
        builtInPrefixes.put("xml:", Namespaces.XML.toString());
    }

    /**
     * Constructs an {@link AbstractCreateEntitiesChangeListGenerator}.
     * @param entityType The type of entities that are created by this change generator.  Not {@code null}.
     * @param sourceText The set of browser text strings that correspond to short names of the fresh entities that will
     * be created.  Not {@code null}.  May be empty.
     * @param parents The parent entities.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public AbstractCreateEntitiesChangeListGenerator(@Nonnull EntityType<E> entityType,
                                                     @Nonnull String sourceText,
                                                     @Nonnull String langTag,
                                                     @Nonnull ImmutableSet<P> parents,
                                                     @Nonnull OWLDataFactory dataFactory,
                                                     @Nonnull MessageFormatter msg,
                                                     @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        this.entityType = entityType;
        this.sourceText = sourceText;
        this.langTag = langTag;
        this.parents = parents;
        this.dataFactory = dataFactory;
        this.msg = msg;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @Override
    public OntologyChangeList<Set<E>> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Set<E>> builder = new OntologyChangeList.Builder<>();
        Set<E> freshEntities = new HashSet<>();
        EntityShortFormsParser parser = new EntityShortFormsParser();
        List<String> shortForms = parser.parseShortForms(sourceText);
        for (String bt : shortForms) {
            String browserText = bt.trim();
            Optional<String> builtInPrefix = getBuiltInPrefix(browserText);
            E freshEntity;
            if(builtInPrefix.isPresent()) {
                String expanded = expandBuiltInPrefix(browserText);
                freshEntity = DataFactory.getOWLEntity(entityType, expanded);
            }
            else {
                var freshEntityIri = FreshEntityIri.get(browserText,
                                   langTag.trim(),
                                   "",
                                   parents.stream().map(OWLEntity::getIRI).collect(toImmutableSet()));
                freshEntity = DataFactory.getOWLEntity(entityType, freshEntityIri.getIri());
                var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
                builder.add(AddAxiomChange.of(ontologyId, dataFactory.getOWLDeclarationAxiom(freshEntity)));
            }
            for(OWLAxiom axiom : createParentPlacementAxioms(freshEntity, context, parents)) {
                var ontologyId = defaultOntologyIdManager.getDefaultOntologyId();
                builder.add(AddAxiomChange.of(ontologyId, axiom));
            }
            freshEntities.add(freshEntity);
        }
        return builder.build(freshEntities);
    }


    private Optional<String> getBuiltInPrefix(String browserText) {
        for(String prefixName : builtInPrefixes.keySet()) {
            if(browserText.startsWith(prefixName)) {
                return Optional.of(prefixName);
            }
        }
        return Optional.empty();
    }

    private String expandBuiltInPrefix(String browserText) {
        for(String prefixName : builtInPrefixes.keySet()) {
            if(browserText.startsWith(prefixName)) {
                String prefix = builtInPrefixes.get(prefixName);
                return prefix + browserText.substring(prefixName.length());
            }
        }
        return browserText;
    }


    @Override
    public Set<E> getRenamedResult(Set<E> result, RenameMap renameMap) {
        return renameMap.getRenamedEntities(result);
    }

    /**
     * Creates any extra axioms that are necessary to set up the "parent" association with the specified fresh entity.
     * @param freshEntity The fresh entity that was created. Not {@code null}.
     * @param context The change generation context. Not {@code null}.
     * @param parents The optional parents. Not {@code null}.
     * @return A possibly empty set of axioms representing axioms that need to be added to the project ontologies to
     * associate the specified fresh entity with its optional parent.  Not {@code null}.
     */
    protected abstract Set<? extends OWLAxiom> createParentPlacementAxioms(E freshEntity,
                                                                           ChangeGenerationContext context,
                                                                           ImmutableSet<P> parents);

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Set<E>> result) {
        Set<E> entities = result.getSubject();
        int entityCount = entities.size();
        String mainMsg;
        if(entityCount == 1) {
            mainMsg = msg.format("Created {0} {1}",
                                       entityType.getPrintName().toLowerCase(),
                                       entities);
            if (!parents.isEmpty()) {
                mainMsg += msg.format(" {0} {1}", getSingularRelationship(), parents);
            }
        }
        else {
            mainMsg = msg.format("Created {0} {1}",
                              entityType.getPluralPrintName().toLowerCase(),
                              entities);
            if (!parents.isEmpty()) {
                mainMsg += msg.format(" {0} {1}", getPluralRelationship(), parents);
            }
        }
        return mainMsg;
    }

    private String getSingularRelationship() {
        if(entityType.equals(CLASS)) {
            return "as a subclass of";
        }
        else if(entityType.equals(OBJECT_PROPERTY) ||
                entityType.equals(DATA_PROPERTY) ||
                entityType.equals(ANNOTATION_PROPERTY)) {
            return "as a sub-property of";
        }
        else if(entityType.equals(NAMED_INDIVIDUAL)) {
            return "as an instance of";
        }
        else if(entityType.equals(DATATYPE)) {
            return "as a subtype of";
        }
        else {
            return "as a child of";
        }
    }

    private String getPluralRelationship() {
        if(entityType.equals(CLASS)) {
            return "as subclasses of";
        }
        else if(entityType.equals(OBJECT_PROPERTY) ||
                entityType.equals(DATA_PROPERTY) ||
                entityType.equals(ANNOTATION_PROPERTY)) {
            return "as sub-properties of";
        }
        else if(entityType.equals(NAMED_INDIVIDUAL)) {
            return "as instances of";
        }
        else if(entityType.equals(DATATYPE)) {
            return "as subtypes of";
        }
        else {
            return "as children of";
        }
    }
}

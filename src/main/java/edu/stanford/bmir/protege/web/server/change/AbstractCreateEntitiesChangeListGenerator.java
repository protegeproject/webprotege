package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.Namespaces;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private EntityType<E> entityType;

    private Set<String> browserTexts;

    private Optional<P> parent;


    private static Map<String, String> builtInPrefixes = new HashMap<String, String>();

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
     * @param browserTexts The set of browser text strings that correspond to short names of the fresh entities that will
     * be created.  Not {@code null}.  May be empty.
     * @param parent The parent entity.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public AbstractCreateEntitiesChangeListGenerator(EntityType<E> entityType, Set<String> browserTexts, Optional<P> parent) {
        this.entityType = checkNotNull(entityType);
        this.browserTexts = new HashSet<String>(checkNotNull(browserTexts));
        this.parent = checkNotNull(parent);
    }

    @Override
    public OntologyChangeList<Set<E>> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<Set<E>> builder = new OntologyChangeList.Builder<Set<E>>();
        Set<E> freshEntities = new HashSet<E>();
        for (String bt : browserTexts) {
            String browserText = bt.trim();
            Optional<String> builtInPrefix = getBuiltInPrefix(browserText);
            E freshEntity;
            if(builtInPrefix.isPresent()) {
                String expanded = expandBuiltInPrefix(browserText);
                freshEntity = DataFactory.getOWLEntity(entityType, expanded);
            }
            else {
                freshEntity = DataFactory.getFreshOWLEntity(entityType, browserText);
                builder.addAxiom(project.getRootOntology(), project.getDataFactory().getOWLDeclarationAxiom(freshEntity));
            }
            for(OWLAxiom axiom : createParentPlacementAxioms(freshEntity, project, context, parent)) {
                builder.addAxiom(project.getRootOntology(), axiom);
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
        return Optional.absent();
    }

    private String expandBuiltInPrefix(String browserText) {
        for(String prefixName : builtInPrefixes.keySet()) {
            if(browserText.startsWith(prefixName)) {
                String prefix = builtInPrefixes.get(prefixName);
                StringBuilder sb = new StringBuilder();
                sb.append(prefix);
                sb.append(browserText.substring(prefixName.length()));
                return sb.toString();
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
     * @param project The reference project that the changes take place in. Not {@code null}.
     * @param context The change generation context. Not {@code null}.
     * @param parent The optional parent. Not {@code null}.
     * @return A possibly empty set of axioms representing axioms that need to be added to the project ontologies to
     * associate the specified fresh entity with its optional parent.  Not {@code null}.
     */
    protected abstract Set<OWLAxiom> createParentPlacementAxioms(E freshEntity, OWLAPIProject project, ChangeGenerationContext context, Optional<P> parent);





//    @Override
//    public GeneratedOntologyChanges<E> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
//        OWLClass freshClass = DataFactory.getFreshOWLEntity(EntityType.CLASS, browserText);
//
//        GeneratedOntologyChanges.Builder<OWLClass> builder = new GeneratedOntologyChanges.Builder<OWLClass>();
//
//        builder.add(new AddAxiom(project.getRootOntology(), DataFactory.get().getOWLDeclarationAxiom(freshClass)));
//        builder.add(new AddAxiom(project.getRootOntology(), DataFactory.get().getOWLSubClassOfAxiom(freshClass, superClass)));
//
//        return builder.build(freshClass);
//    }

//    @Override
//    public OWLClass getRenamedResult(OWLClass result, RenameMap renameMap) {
//        return renameMap.getRenamedEntity(result);
//    }
}

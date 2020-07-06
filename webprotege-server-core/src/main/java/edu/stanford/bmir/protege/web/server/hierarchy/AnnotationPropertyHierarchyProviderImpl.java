package edu.stanford.bmir.protege.web.server.hierarchy;


import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 23, 2009<br><br>
 */
@ProjectSingleton
public class AnnotationPropertyHierarchyProviderImpl extends AbstractHierarchyProvider<OWLAnnotationProperty> implements AnnotationPropertyHierarchyProvider {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationPropertyHierarchyProviderImpl.class);

    private final ProjectId projectId;

    private final Set<OWLAnnotationProperty> roots;

    private final OWLAnnotationPropertyProvider annotationPropertyProvider;

    private final ProjectSignatureByTypeIndex projectSignatureByTypeIndex;

    private final ProjectOntologiesIndex projectOntologiesIndex;

    private final SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxioms;

    private final SubAnnotationPropertyAxiomsBySuperPropertyIndex subAnnotationPropertyAxiomsBySuperPropertyIndex;

    private final EntitiesInProjectSignatureIndex entitiesInSignature;

    private boolean stale = true;


    @Inject
    public AnnotationPropertyHierarchyProviderImpl(ProjectId projectId,
                                                   OWLAnnotationPropertyProvider annotationPropertyProvider,
                                                   ProjectSignatureByTypeIndex projectSignatureByTypeIndex,
                                                   ProjectOntologiesIndex projectOntologiesIndex,
                                                   SubAnnotationPropertyAxiomsBySubPropertyIndex subAnnotationPropertyAxioms,
                                                   SubAnnotationPropertyAxiomsBySuperPropertyIndex subAnnotationPropertyAxiomsBySuperPropertyIndex,
                                                   EntitiesInProjectSignatureIndex entitiesInSignature) {
        this.projectId = projectId;
        this.projectSignatureByTypeIndex = projectSignatureByTypeIndex;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.subAnnotationPropertyAxioms = subAnnotationPropertyAxioms;
        this.subAnnotationPropertyAxiomsBySuperPropertyIndex = subAnnotationPropertyAxiomsBySuperPropertyIndex;
        this.entitiesInSignature = entitiesInSignature;
        this.roots = new HashSet<>();
        this.annotationPropertyProvider = annotationPropertyProvider;
    }

    private void rebuildIfNecessary() {
        if(this.stale) {
            rebuildRoots();
        }
    }

    public Collection<OWLAnnotationProperty> getRoots() {
        rebuildIfNecessary();
        return Collections.unmodifiableSet(roots);
    }

    public boolean containsReference(OWLAnnotationProperty object) {
        return entitiesInSignature.containsEntityInSignature(object);
    }


    public Collection<OWLAnnotationProperty> getChildren(OWLAnnotationProperty object) {
        rebuildIfNecessary();
        return projectOntologiesIndex.getOntologyIds()
                              .flatMap(ontId -> subAnnotationPropertyAxiomsBySuperPropertyIndex.getAxiomsForSuperProperty(object, ontId))
                              .map(OWLSubAnnotationPropertyOfAxiom::getSubProperty)
                              .filter(prop -> !getAncestors(prop).contains(prop))
                              .collect(toImmutableSet());
    }


    public Collection<OWLAnnotationProperty> getParents(OWLAnnotationProperty object) {
        rebuildIfNecessary();
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> subAnnotationPropertyAxioms.getSubPropertyOfAxioms(object, ontId))
                                     .map(OWLSubAnnotationPropertyOfAxiom::getSuperProperty)
                                     .collect(toImmutableSet());
    }


    public void dispose() {
        super.dispose();
    }


    public void handleChanges(List<OntologyChange> changes) {
        Set<OWLAnnotationProperty> properties = new HashSet<>(getPropertiesReferencedInChange(changes));
        for (OWLAnnotationProperty prop : properties) {
            if (isRoot(prop)) {
                roots.add(prop);
            }
            else {
                if (getAncestors(prop).contains(prop)) {
                    roots.add(prop);
                    for (OWLAnnotationProperty anc : getAncestors(prop)) {
                        if (getAncestors(anc).contains(prop)) {
                            roots.add(anc);
                        }
                    }
                }
                else {
                    roots.remove(prop);
                }
            }
        }
    }


    private Set<OWLAnnotationProperty> getPropertiesReferencedInChange(List<OntologyChange> changes){
        final Set<OWLAnnotationProperty> props = new HashSet<>();
        for (OntologyChange chg : changes){
            if(chg.isAxiomChange()){
                chg.getAxiomOrThrow().accept(new OWLAxiomVisitorAdapter(){
                    public void visit(OWLSubAnnotationPropertyOfAxiom owlSubAnnotationPropertyOfAxiom) {
                        props.add(owlSubAnnotationPropertyOfAxiom.getSubProperty());
                        props.add(owlSubAnnotationPropertyOfAxiom.getSuperProperty());
                    }

                    public void visit(OWLDeclarationAxiom owlDeclarationAxiom) {
                        if (owlDeclarationAxiom.getEntity().isOWLAnnotationProperty()){
                            props.add(owlDeclarationAxiom.getEntity().asOWLAnnotationProperty());
                        }
                    }
                });
            }
        }
        return props;
    }


    private boolean isRoot(OWLAnnotationProperty prop) {
        // We deem a property to be a root property if it doesn't have
        // any super properties (i.e. it is not on
        // the LHS of a subproperty axiom
        // Assume the property is a root property to begin with
        boolean isRoot = getParents(prop).isEmpty();
        // Additional condition: If we have  P -> Q and Q -> P, then
        // there is no path to the root, so put P and Q as root properties
        // Collapse any cycles and force properties that are equivalent
        // through cycles to appear at the root.
        return isRoot && containsReference(prop) || getAncestors(prop).contains(prop);
    }


    private void rebuildRoots() {
        this.stale = false;
        roots.clear();
        logger.info("{} Rebuilding annotation property hierarchy", projectId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        projectSignatureByTypeIndex.getSignature(EntityType.ANNOTATION_PROPERTY)
                            .filter(this::isRoot)
                            .forEach(roots::add);
        OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS.stream()
                .map(annotationPropertyProvider::getOWLAnnotationProperty)
                .forEach(roots::add);
        logger.info("{} Rebuilt annotation property hierarchy provider in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
    }
}

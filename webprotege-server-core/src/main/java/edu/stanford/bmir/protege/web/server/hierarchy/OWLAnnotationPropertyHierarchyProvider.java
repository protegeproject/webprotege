package edu.stanford.bmir.protege.web.server.hierarchy;


import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
public class OWLAnnotationPropertyHierarchyProvider extends AbstractHierarchyProvider<OWLAnnotationProperty> {

    private static final Logger logger = LoggerFactory.getLogger(OWLAnnotationPropertyHierarchyProvider.class);

    private final ProjectId projectId;

    private final OWLOntology rootOntology;

    private final Set<OWLAnnotationProperty> roots;

    private final OWLAnnotationPropertyProvider annotationPropertyProvider;


    @Inject
    public OWLAnnotationPropertyHierarchyProvider(ProjectId projectId, @RootOntology OWLOntology rootOntology, OWLAnnotationPropertyProvider annotationPropertyProvider) {
        this.projectId = projectId;
        this.roots = new HashSet<>();
        this.rootOntology = rootOntology;
        this.annotationPropertyProvider = annotationPropertyProvider;
        rebuildRoots();
        fireHierarchyChanged();
    }

    public Set<OWLAnnotationProperty> getRoots() {
        return Collections.unmodifiableSet(roots);
    }

    private Collection<OWLOntology> getOntologies() {
        return rootOntology.getImportsClosure();
    }

    public boolean containsReference(OWLAnnotationProperty object) {
        return rootOntology.containsEntityInSignature(object, Imports.INCLUDED);
    }


    public Set<OWLAnnotationProperty> getChildren(OWLAnnotationProperty object) {
        Set<OWLAnnotationProperty> result = new HashSet<>();
        for (OWLOntology ont : getOntologies()) {
            for (OWLSubAnnotationPropertyOfAxiom ax : ont.getAxioms(AxiomType.SUB_ANNOTATION_PROPERTY_OF)) {
                if (ax.getSuperProperty().equals(object)){
                    OWLAnnotationProperty subProp = ax.getSubProperty();
                    // prevent cycles
                    if (!getAncestors(subProp).contains(subProp)) {
                        result.add(subProp);
                    }
                }
            }
        }
        return result;
    }


    public Set<OWLAnnotationProperty> getEquivalents(OWLAnnotationProperty object) {
        Set<OWLAnnotationProperty> result = new HashSet<>();
        Set<OWLAnnotationProperty> ancestors = getAncestors(object);
        if (ancestors.contains(object)) {
            for (OWLAnnotationProperty anc : ancestors) {
                if (getAncestors(anc).contains(object)) {
                    result.add(anc);
                }
            }
        }
        result.remove(object);
        return result;
    }


    public Set<OWLAnnotationProperty> getParents(OWLAnnotationProperty object) {
        Set<OWLAnnotationProperty> result = new HashSet<>();
        for (OWLOntology ont : getOntologies()) {
            for (OWLSubAnnotationPropertyOfAxiom ax : ont.getSubAnnotationPropertyOfAxioms(object)){
                if (ax.getSubProperty().equals(object)){
                    OWLAnnotationProperty superProp = ax.getSuperProperty();
                    result.add(superProp);
                }
            }
        }
        return result;
    }


    public void dispose() {
        super.dispose();
    }


    public void handleChanges(List<? extends OWLOntologyChange> changes) {
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
                            fireNodeChanged(anc);
                        }
                    }
                }
                else {
                    roots.remove(prop);
                }
            }
            fireNodeChanged(prop);
        }
    }


    private Set<OWLAnnotationProperty> getPropertiesReferencedInChange(List<? extends OWLOntologyChange> changes){
        final Set<OWLAnnotationProperty> props = new HashSet<>();
        for (OWLOntologyChange chg : changes){
            if(chg.isAxiomChange()){
                chg.getAxiom().accept(new OWLAxiomVisitorAdapter(){
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
        roots.clear();
        logger.info("{} Rebuilding annotation property hierarchy", projectId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        Collection<OWLAnnotationProperty> annotationProperties = rootOntology.getAnnotationPropertiesInSignature(Imports.INCLUDED);
        annotationProperties.stream()
                            .filter(this::isRoot)
                            .forEach(roots::add);
        OWLRDFVocabulary.BUILT_IN_ANNOTATION_PROPERTY_IRIS.stream()
                .map(annotationPropertyProvider::getOWLAnnotationProperty)
                .forEach(roots::add);
        logger.info("{} Rebuilt annotation property hierarchy provider in {} ms", projectId, stopwatch.elapsed(MILLISECONDS));
    }
}

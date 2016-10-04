package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.protege.editor.owl.model.hierarchy.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jan-2007<br><br>
 */
public abstract class AbstractOWLPropertyHierarchyProvider<R extends OWLPropertyRange, E extends OWLPropertyExpression, P extends E> extends AbstractOWLObjectHierarchyProvider<P> {

    private final OWLOntology rootOntology;

    private Set<P> subPropertiesOfRoot;

    private P root;

    public AbstractOWLPropertyHierarchyProvider(@RootOntology OWLOntology rootOntology, P root) {
        this.subPropertiesOfRoot = new HashSet<P>();
        this.rootOntology = rootOntology;
        this.root = root;
        rebuildRoots();
        fireHierarchyChanged();
    }


    public void dispose() {
        super.dispose();
    }


    public void handleChanges(List<? extends OWLOntologyChange> changes) {
        Set<P> properties = new HashSet<P>(getPropertiesReferencedInChange(changes));
        for (P prop : properties) {
            if (isSubPropertyOfRoot(prop)) {
                subPropertiesOfRoot.add(prop);
                fireNodeChanged(getRoot());
            }
            else {
                if (getAncestors(prop).contains(prop)) {
                    subPropertiesOfRoot.add(prop);
                    for (P anc : getAncestors(prop)) {
                        if (getAncestors(anc).contains(prop)) {
                            subPropertiesOfRoot.add(anc);
                            fireNodeChanged(anc);
                        }
                    }
                }
                else {
                    subPropertiesOfRoot.remove(prop);
                }
            }
            fireNodeChanged(prop);
        }
        fireNodeChanged(getRoot());
    }


    protected abstract Set<P> getPropertiesReferencedInChange(List<? extends OWLOntologyChange> changes);

    private Set<OWLOntology> getOntologies() {
        return rootOntology.getImportsClosure();
    }

    private boolean isSubPropertyOfRoot(P prop) {

        if (prop.equals(getRoot())){
            return false;
        }

        // We deem a property to be a sub of the top property if this is asserted
        // or if no named superproperties are asserted
        final Set<P> parents = getParents(prop);
        if (parents.isEmpty() || parents.contains(getRoot())){
            for (OWLOntology ont : getOntologies()) {
                if (containsReference(ont, prop)) {
                    return true;
                }
            }
        }
        // Additional condition: If we have  P -> Q and Q -> P, then
        // there is no path to the root, so put P and Q as root properties
        // Collapse any cycles and force properties that are equivalent
        // through cycles to appear at the root.
        return getAncestors(prop).contains(prop);
    }


    private void rebuildRoots() {
        subPropertiesOfRoot.clear();
        for (OWLOntology ontology : getOntologies()) {
            for (P prop : getReferencedProperties(ontology)) {
                if (isSubPropertyOfRoot(prop)) {
                    subPropertiesOfRoot.add(prop);
                }
            }
        }
    }


    protected abstract boolean containsReference(OWLOntology ont, P prop);


    /**
     * Gets the relevant properties in the specified ontology that are contained
     * within the property hierarchy.  For example, for an object property hierarchy
     * this would constitute the set of referenced object properties in the specified
     * ontology.
     * @param ont The ontology
     */
    protected abstract Set<? extends P> getReferencedProperties(OWLOntology ont);


    protected abstract Set<? extends OWLSubPropertyAxiom> getSubPropertyAxiomForRHS(P prop, OWLOntology ont);


    protected final P getRoot() {
        return root;
    }


    /**
     * Gets the objects that represent the roots of the hierarchy.
     */
    public Set<P> getRoots() {
        return Collections.singleton(getRoot());
    }

    public boolean containsReference(P object) {
        for (OWLOntology ont : getOntologies()) {
            if (getReferencedProperties(ont).contains(object)) {
                return true;
            }
        }
        return false;
    }


    public Set<P> getChildren(P object) {
        if (object.equals(getRoot())){
            return Collections.unmodifiableSet(subPropertiesOfRoot);
        }

        final Set<P> result = new HashSet<P>();
        for (E subProp : EntitySearcher.getSubProperties(object, getOntologies())){
            // Don't add the sub property if it is a parent of
            // itself - i.e. prevent cycles
            if (!subProp.isAnonymous() &&
                    !getAncestors((P)subProp).contains(subProp)) {
                result.add((P)subProp);
            }
        }
        return result;
    }


    public Set<P> getEquivalents(P object) {
        Set<P> result = new HashSet<P>();
        Set<P> ancestors = getAncestors(object);
        if (ancestors.contains(object)) {
            for (P anc : ancestors) {
                if (getAncestors(anc).contains(object)) {
                    result.add(anc);
                }
            }
        }

        for (E prop : EntitySearcher.getEquivalentProperties(object, getOntologies())) {
            if (!prop.isAnonymous()) {
                result.add((P)prop);
            }
        }

        result.remove(object);
        return result;
    }


    public Set<P> getParents(P object) {
        if (object.equals(getRoot())){
            return Collections.emptySet();
        }

        Set<P> result = new HashSet<P>();
        for (E prop : EntitySearcher.getSuperProperties(object, getOntologies())) {
            if (!prop.isAnonymous()) {
                result.add((P) prop);
            }
        }
        if (result.isEmpty() && isReferenced(object)){
            result.add(getRoot());
        }

        return result;
    }


    private boolean isReferenced(P e) {
        return e.accept(new IsReferencePropertyExpressionVisitor());
    }

    private class IsReferencePropertyExpressionVisitor implements OWLPropertyExpressionVisitorEx<Boolean> {

        public Boolean visit(OWLObjectProperty property) {
            return isReferenced(property);
        }

        public Boolean visit(OWLObjectInverseOf property) {
            return property.getInverse().accept(this);
        }

        public Boolean visit(OWLDataProperty property) {
            return isReferenced(property);
        }

        @Nonnull
        @Override
        public Boolean visit(OWLAnnotationProperty property) {
            return isReferenced(property);
        }

        private boolean isReferenced(OWLEntity e) {
            for (OWLOntology ontology : getOntologies()) {
                if (ontology.containsEntityInSignature(e)) {
                    return true;
                }
            }
            return false;
        }
    }
}

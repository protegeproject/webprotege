package edu.stanford.bmir.protege.web.server.hierarchy;

import org.protege.editor.owl.model.hierarchy.OWLObjectHierarchyProvider;
import org.semanticweb.owlapi.model.OWLObjectProperty;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class ObjectPropertyObjectPropertyAncestorChecker implements HasHasAncestor<OWLObjectProperty, OWLObjectProperty> {

    private OWLObjectHierarchyProvider<OWLObjectProperty> hierarchyProvider;

    public ObjectPropertyObjectPropertyAncestorChecker(OWLObjectHierarchyProvider<OWLObjectProperty>
                                                               hierarchyProvider) {
        this.hierarchyProvider = hierarchyProvider;
    }

    @Override
    public boolean hasAncestor(OWLObjectProperty node, OWLObjectProperty node2) {
        return node.equals(node2) || hierarchyProvider.getAncestors(node).contains(node2);
    }
}

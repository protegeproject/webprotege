package edu.stanford.bmir.protege.web.server.hierarchy;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public class ObjectPropertyObjectPropertyAncestorChecker implements HasHasAncestor<OWLObjectProperty, OWLObjectProperty> {

    private HierarchyProvider<OWLObjectProperty> hierarchyProvider;

    @Inject
    public ObjectPropertyObjectPropertyAncestorChecker(HierarchyProvider<OWLObjectProperty>
                                                               hierarchyProvider) {
        this.hierarchyProvider = hierarchyProvider;
    }

    @Override
    public boolean hasAncestor(OWLObjectProperty node, OWLObjectProperty node2) {
        return node.equals(node2) || hierarchyProvider.getAncestors(node).contains(node2);
    }
}

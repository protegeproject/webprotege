package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.EntityHierarchyNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.DataPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.DATA_PROPERTY_HIERARCHY;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLDataPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLDataProperty> {

    private final HierarchyProvider<OWLDataProperty> hierarchyProvider;

    private final EntityHierarchyNodeRenderer renderer;

    @Inject
    public OWLDataPropertyHierarchyChangeComputer(ProjectId projectId, HierarchyProvider<OWLDataProperty> hierarchyProvider, EntityHierarchyNodeRenderer renderer) {
        super(projectId, EntityType.DATA_PROPERTY, hierarchyProvider, DATA_PROPERTY_HIERARCHY, renderer);
        this.hierarchyProvider = hierarchyProvider;
        this.renderer = renderer;
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLDataProperty child, OWLDataProperty parent) {
        RemoveEdge<EntityHierarchyNode> removeEdge = new RemoveEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent)),
                new GraphNode<>(renderer.render(child))
        ));
        return Arrays.asList(
                new EntityHierarchyChangedEvent(getProjectId(), DATA_PROPERTY_HIERARCHY, new GraphModelChangedEvent<>(Collections.singletonList(removeEdge))),
                new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, DATA_PROPERTY_HIERARCHY)
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLDataProperty child, OWLDataProperty parent) {
        AddEdge<EntityHierarchyNode> addEdge = new AddEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent), hierarchyProvider.getChildren(parent).isEmpty()),
                new GraphNode<>(renderer.render(child), hierarchyProvider.getChildren(child).isEmpty())
        ));
        return Arrays.asList(
                new EntityHierarchyChangedEvent(getProjectId(), DATA_PROPERTY_HIERARCHY, new GraphModelChangedEvent<>(Collections.singletonList(addEdge))),
                new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, DATA_PROPERTY_HIERARCHY)
        );
    }
}

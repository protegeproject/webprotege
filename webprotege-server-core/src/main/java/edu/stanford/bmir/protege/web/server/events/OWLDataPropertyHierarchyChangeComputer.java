package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.DataPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.DATA_PROPERTY_HIERARCHY;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLDataPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLDataProperty> {

    private final DataPropertyHierarchyProvider hierarchyProvider;

    private final EntityNodeRenderer renderer;

    @Inject
    public OWLDataPropertyHierarchyChangeComputer(ProjectId projectId, DataPropertyHierarchyProvider hierarchyProvider, EntityNodeRenderer renderer) {
        super(projectId, EntityType.DATA_PROPERTY, hierarchyProvider, DATA_PROPERTY_HIERARCHY, renderer);
        this.hierarchyProvider = hierarchyProvider;
        this.renderer = renderer;
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLDataProperty child, OWLDataProperty parent) {
        RemoveEdge<EntityNode> removeEdge = new RemoveEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent)),
                new GraphNode<>(renderer.render(child))
        ));
        return Collections.singletonList(
                new EntityHierarchyChangedEvent(getProjectId(), DATA_PROPERTY_HIERARCHY, new GraphModelChangedEvent<>(Collections.singletonList(removeEdge)))
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLDataProperty child, OWLDataProperty parent) {
        AddEdge<EntityNode> addEdge = new AddEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent), hierarchyProvider.isLeaf(parent)),
                new GraphNode<>(renderer.render(child), hierarchyProvider.isLeaf(child))
        ));
        return Collections.singletonList(
                new EntityHierarchyChangedEvent(getProjectId(), DATA_PROPERTY_HIERARCHY, new GraphModelChangedEvent<>(Collections.singletonList(addEdge)))
        );
    }
}

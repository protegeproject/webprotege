package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.ObjectPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.OBJECT_PROPERTY_HIERARCHY;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLObjectPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLObjectProperty> {

    @Nonnull
    private final ObjectPropertyHierarchyProvider hierarchyProvider;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Inject
    public OWLObjectPropertyHierarchyChangeComputer(@Nonnull ProjectId projectId,
                                                    @Nonnull ObjectPropertyHierarchyProvider hierarchyProvider,
                                                    @Nonnull EntityNodeRenderer renderer) {
        super(projectId, EntityType.OBJECT_PROPERTY, hierarchyProvider, OBJECT_PROPERTY_HIERARCHY, renderer);
        this.hierarchyProvider = hierarchyProvider;
        this.renderer = renderer;
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLObjectProperty child, OWLObjectProperty parent) {
        RemoveEdge<EntityNode> removeEdge = new RemoveEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent)),
                new GraphNode<>(renderer.render(child))
        ));
        return Collections.singletonList(new EntityHierarchyChangedEvent(getProjectId(),
                                                                         OBJECT_PROPERTY_HIERARCHY,
                                                                         new GraphModelChangedEvent<>(Collections.singletonList(
                                                                                 removeEdge))));
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLObjectProperty child, OWLObjectProperty parent) {
        AddEdge<EntityNode> addEdge = new AddEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent), hierarchyProvider.isLeaf(parent)),
                new GraphNode<>(renderer.render(child), hierarchyProvider.isLeaf(child))
        ));
        return Collections.singletonList(new EntityHierarchyChangedEvent(getProjectId(),
                                                                         OBJECT_PROPERTY_HIERARCHY,
                                                                         new GraphModelChangedEvent<>(Collections.singletonList(
                                                                                 addEdge))));
    }
}

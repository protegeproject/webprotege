package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.CLASS_HIERARCHY;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLClassHierarchyChangeComputer extends HierarchyChangeComputer<OWLClass> {

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Inject
    public OWLClassHierarchyChangeComputer(@Nonnull ProjectId projectId,
                                           @Nonnull HierarchyProvider<OWLClass> hierarchyProvider, @Nonnull EntityNodeRenderer renderer, @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        super(projectId, EntityType.CLASS, hierarchyProvider, CLASS_HIERARCHY, renderer);
        this.renderer = checkNotNull(renderer);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLClass child, OWLClass parent) {
        RemoveEdge<EntityNode> removeEdge = new RemoveEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent)),
                new GraphNode<>(renderer.render(child))
        ));
        return Arrays.asList(
                new EntityHierarchyChangedEvent(getProjectId(), CLASS_HIERARCHY, new GraphModelChangedEvent<>(Collections.singletonList(removeEdge)))
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLClass child, OWLClass parent) {
        AddEdge<EntityNode> addEdge = new AddEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent), classHierarchyProvider.getChildren(parent).isEmpty()),
                new GraphNode<>(renderer.render(child), classHierarchyProvider.getChildren(child).isEmpty())
        ));
        return Arrays.asList(
                new EntityHierarchyChangedEvent(getProjectId(), CLASS_HIERARCHY, new GraphModelChangedEvent<>(Collections.singletonList(addEdge)))
        );
    }
}

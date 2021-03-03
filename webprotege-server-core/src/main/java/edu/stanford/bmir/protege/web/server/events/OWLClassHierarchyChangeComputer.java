package edu.stanford.bmir.protege.web.server.events;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
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

    private final EntityHierarchyChangedEventProxyFactory proxyFactory;

    @Inject
    public OWLClassHierarchyChangeComputer(@Nonnull ProjectId projectId,
                                           @Nonnull ClassHierarchyProvider classHierarchyProvider,
                                           @Nonnull EntityNodeRenderer renderer,
                                           EntityHierarchyChangedEventProxyFactory proxyFactory) {
        super(projectId, EntityType.CLASS, classHierarchyProvider, CLASS_HIERARCHY, renderer);
        this.renderer = checkNotNull(renderer);
        this.classHierarchyProvider = checkNotNull(classHierarchyProvider);
        this.proxyFactory = checkNotNull(proxyFactory);
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createRemovedEvents(OWLClass child, OWLClass parent) {
        var removeEdge = new RemoveEdge<>(
                new GraphEdge<>(
                        new GraphNode<>(parent, classHierarchyProvider.isLeaf(parent)),
                        new GraphNode<>(child, classHierarchyProvider.isLeaf(child))
                )
        );
        var event = new GraphModelChangedEvent<>(ImmutableList.of(removeEdge));
        var proxyEvent = proxyFactory.create(event, classHierarchyProvider, CLASS_HIERARCHY);
        return ImmutableList.of(proxyEvent);
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createAddedEvents(OWLClass child, OWLClass parent) {
        var addEdge = new AddEdge<>(
                new GraphEdge<>(
                        new GraphNode<>(parent, classHierarchyProvider.isLeaf(parent)),
                        new GraphNode<>(child, classHierarchyProvider.isLeaf(child))
                )
        );
        var event = new GraphModelChangedEvent<>(ImmutableList.of(addEdge));
        var proxyEvent = proxyFactory.create(event, classHierarchyProvider, CLASS_HIERARCHY);
        return ImmutableList.of(proxyEvent);
    }
}

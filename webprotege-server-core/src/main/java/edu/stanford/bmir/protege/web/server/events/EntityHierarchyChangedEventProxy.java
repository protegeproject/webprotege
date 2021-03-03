package edu.stanford.bmir.protege.web.server.events;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.GraphNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-05
 */
public class EntityHierarchyChangedEventProxy implements HighLevelProjectEventProxy {

    @Nonnull
    private final GraphModelChangedEvent<OWLEntity> graphModelChangedEvent;

    @Nonnull
    private final GraphNodeRenderer renderer;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final HierarchyProvider<OWLEntity> hierarchyProvider;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final HierarchyId hierarchyId;

    @AutoFactory
    public EntityHierarchyChangedEventProxy(@Nonnull GraphModelChangedEvent<? extends OWLEntity> graphModelChangedEvent,
                                            @Provided @Nonnull GraphNodeRenderer renderer,
                                            @Provided @Nonnull EntityNodeRenderer entityNodeRenderer,
                                            @Nonnull HierarchyProvider<? extends OWLEntity> classHierarchyProvider,
                                            @Provided @Nonnull ProjectId projectId,
                                            @Nonnull HierarchyId hierarchyId) {
        this.graphModelChangedEvent = (GraphModelChangedEvent) graphModelChangedEvent;
        this.renderer = renderer;
        this.entityNodeRenderer = entityNodeRenderer;
        this.hierarchyProvider = (HierarchyProvider) classHierarchyProvider;
        this.projectId = projectId;
        this.hierarchyId = hierarchyId;
    }

    @Nonnull
    @Override
    public ProjectEvent<?> asProjectEvent() {
        var mappedChanges = ImmutableList.<GraphModelChange<EntityNode>>builder();
        graphModelChangedEvent.getChanges()
             .forEach(chg -> chg.accept(new GraphModelChangeVisitor<OWLEntity>() {
                 @Override
                 public void visit(AddRootNode<OWLEntity> addRootNode) {
                     var entity = addRootNode.getNode().getUserObject();
                     var mappedChg = new AddRootNode<>(
                             renderer.toGraphNode(entity, hierarchyProvider));
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(RemoveRootNode<OWLEntity> removeRootNode) {
                     var entity = removeRootNode.getNode().getUserObject();
                     var mappedChg = new RemoveRootNode<>(
                             renderer.toGraphNode(entity, hierarchyProvider));
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(AddEdge<OWLEntity> addEdge) {
                     var pred = addEdge.getPredecessor().getUserObject();
                     var succ = addEdge.getSuccessor().getUserObject();
                     var mappedChg = new AddEdge<>(
                                new GraphEdge<>(
                                        renderer.toGraphNode(pred, hierarchyProvider),
                                        renderer.toGraphNode(succ, hierarchyProvider)
                                )
                             );
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(RemoveEdge<OWLEntity> removeEdge) {
                     var pred = removeEdge.getPredecessor().getUserObject();
                     var succ = removeEdge.getSuccessor().getUserObject();
                     var mappedChg = new RemoveEdge<>(
                             new GraphEdge<>(
                                     renderer.toGraphNode(pred, hierarchyProvider),
                                     renderer.toGraphNode(succ, hierarchyProvider)
                             )
                     );
                     mappedChanges.add(mappedChg);
                 }

                 @Override
                 public void visit(UpdateUserObject<OWLEntity> updateUserObject) {
                     var entity = updateUserObject.getUserObject();
                     var mappedChg = new UpdateUserObject<>(entityNodeRenderer.render(entity));
                     mappedChanges.add(mappedChg);
                 }
             }));
        var mappedEvent = new GraphModelChangedEvent<>(mappedChanges.build());
        return new EntityHierarchyChangedEvent(projectId, hierarchyId, mappedEvent);
    }
}

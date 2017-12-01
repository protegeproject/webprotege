package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyModel;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent.ON_BROWSER_TEXT_CHANGED;
import static edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent.ON_ENTITY_DEPRECATED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent.ON_STATUS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent.ON_WATCH_ADDED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent.ON_WATCH_REMOVED;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class EntityHierarchyNodeUpdater {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityHierarchyModel model;

    @Inject
    public EntityHierarchyNodeUpdater(@Nonnull ProjectId projectId,
                                      @Nonnull EntityHierarchyModel model) {
        this.projectId = checkNotNull(projectId);
        this.model = checkNotNull(model);
    }

    public void start(WebProtegeEventBus eventBus) {
        GWT.log("[EntityHierarchyNodeUpdater] Starting to listen for events");
        eventBus.addProjectEventHandler(projectId, ON_WATCH_ADDED, this::handleWatchAdded);
        eventBus.addProjectEventHandler(projectId, ON_WATCH_REMOVED, this::handleWatchRemoved);
        eventBus.addProjectEventHandler(projectId, ON_BROWSER_TEXT_CHANGED, this::handleBrowserTextChanged);
        eventBus.addProjectEventHandler(projectId, ON_COMMENT_POSTED, this::handleCommentPosted);
        eventBus.addProjectEventHandler(projectId, ON_STATUS_CHANGED, this::handleDiscussionThreadStatusChanged);
        eventBus.addProjectEventHandler(projectId, ON_ENTITY_DEPRECATED, this::handleEntityDeprecatedChanged);
    }

    int browserTextEventCounter = 0;

    private void handleBrowserTextChanged(BrowserTextChangedEvent event) {
        browserTextEventCounter++;
        model.getHierarchyNode(event.getEntity()).ifPresent(node -> {
            EntityHierarchyNode updatedNode = new EntityHierarchyNode(
                    node.getEntity(),
                    event.getNewBrowserText(),
                    node.isDeprecated(),
                    node.getWatches(),
                    node.getOpenCommentCount());
            model.updateNode(updatedNode);
        });
    }

    private void handleWatchAdded(WatchAddedEvent event) {
        model.getHierarchyNode(event.getWatch().getEntity()).ifPresent(node -> {
            Set<Watch> updatedWatches = new HashSet<>(node.getWatches());
            updatedWatches.add(event.getWatch());
            updateWatches(node, updatedWatches);
        });
    }

    private void handleWatchRemoved(WatchRemovedEvent event) {
        model.getHierarchyNode(event.getWatch().getEntity()).ifPresent(node -> {
            Set<Watch> updatedWatches = new HashSet<>(node.getWatches());
            updatedWatches.remove(event.getWatch());
            updateWatches(node, updatedWatches);
        });
    }

    private void updateWatches(EntityHierarchyNode node, Set<Watch> updatedWatches) {
        EntityHierarchyNode updatedNode = new EntityHierarchyNode(
                node.getEntity(),
                node.getBrowserText(),
                node.isDeprecated(),
                updatedWatches,
                node.getOpenCommentCount());
        model.updateNode(updatedNode);
    }

    private void handleCommentPosted(CommentPostedEvent event) {
        event.getEntity().ifPresent(entity -> {
            model.getHierarchyNode(entity.getEntity()).ifPresent(node -> {
                EntityHierarchyNode updatedNode = new EntityHierarchyNode(
                        node.getEntity(),
                        node.getBrowserText(),
                        node.isDeprecated(),
                        node.getWatches(),
                        event.getOpenCommentCountForEntity());
                model.updateNode(updatedNode);
            });
        });
    }

    private void handleDiscussionThreadStatusChanged(DiscussionThreadStatusChangedEvent event) {
        event.getEntity().ifPresent(entity -> {
            model.getHierarchyNode(entity).ifPresent(node -> {
                EntityHierarchyNode updatedNode = new EntityHierarchyNode(
                        node.getEntity(),
                        node.getBrowserText(),
                        node.isDeprecated(),
                        node.getWatches(),
                        event.getOpenCommentsCountForEntity());
                model.updateNode(updatedNode);
            });
        });
    }

    private void handleEntityDeprecatedChanged(EntityDeprecatedChangedEvent event) {
            model.getHierarchyNode(event.getEntity()).ifPresent(node -> {
                EntityHierarchyNode updatedNode = new EntityHierarchyNode(
                        node.getEntity(),
                        node.getBrowserText(),
                        event.isDeprecated(),
                        node.getWatches(),
                        node.getOpenCommentCount());
                model.updateNode(updatedNode);
        });
    }
}

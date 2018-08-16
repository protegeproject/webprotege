package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.EntityNodeIndex;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent;
import edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent;
import edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent.ON_BROWSER_TEXT_CHANGED;
import static edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent.ON_ENTITY_DEPRECATED;
import static edu.stanford.bmir.protege.web.shared.issues.CommentPostedEvent.ON_COMMENT_POSTED;
import static edu.stanford.bmir.protege.web.shared.issues.DiscussionThreadStatusChangedEvent.ON_STATUS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent.ON_ENTITY_TAGS_CHANGED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchAddedEvent.ON_WATCH_ADDED;
import static edu.stanford.bmir.protege.web.shared.watches.WatchRemovedEvent.ON_WATCH_REMOVED;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 30 Nov 2017
 */
public class EntityNodeUpdater {

    @Nonnull
    private final ProjectId projectId;

    @Nullable
    private EntityNodeIndex nodeIndex;

    @Inject
    public EntityNodeUpdater(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    /**
     * Start listening for events on the specified event bus in order to keep the specified hierarchy
     * up to date.
     * @param eventBus The event bus on which project changes are broadcast.
     * @param nodeIndex The node index that will be kept up to date.
     */
    public void start(@Nonnull WebProtegeEventBus eventBus,
                      @Nonnull EntityNodeIndex nodeIndex) {
        GWT.log("[EntityNodeUpdater] Starting to listen for events");
        this.nodeIndex = checkNotNull(nodeIndex);
        eventBus.addProjectEventHandler(projectId, ON_WATCH_ADDED, this::handleWatchAdded);
        eventBus.addProjectEventHandler(projectId, ON_WATCH_REMOVED, this::handleWatchRemoved);
        eventBus.addProjectEventHandler(projectId, ON_BROWSER_TEXT_CHANGED, this::handleBrowserTextChanged);
        eventBus.addProjectEventHandler(projectId, ON_COMMENT_POSTED, this::handleCommentPosted);
        eventBus.addProjectEventHandler(projectId, ON_STATUS_CHANGED, this::handleDiscussionThreadStatusChanged);
        eventBus.addProjectEventHandler(projectId, ON_ENTITY_DEPRECATED, this::handleEntityDeprecatedChanged);
        eventBus.addProjectEventHandler(projectId, ON_ENTITY_TAGS_CHANGED, this::handleEntityTagsChanged);
    }

    private void handleBrowserTextChanged(BrowserTextChangedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        nodeIndex.getNode(event.getEntity()).ifPresent(node -> {
            EntityNode updatedNode = EntityNode.get(
                    node.getEntity(),
                    event.getNewBrowserText(),
                    event.getShortForms(),
                    node.isDeprecated(),
                    node.getWatches(),
                    node.getOpenCommentCount(),
                    node.getTags());
            nodeIndex.updateNode(updatedNode);
        });
    }

    private void handleWatchAdded(WatchAddedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        nodeIndex.getNode(event.getWatch().getEntity()).ifPresent(node -> {
            Set<Watch> updatedWatches = new HashSet<>(node.getWatches());
            updatedWatches.add(event.getWatch());
            updateWatches(node, updatedWatches);
        });
    }

    private void handleWatchRemoved(WatchRemovedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        nodeIndex.getNode(event.getWatch().getEntity()).ifPresent(node -> {
            Set<Watch> updatedWatches = new HashSet<>(node.getWatches());
            updatedWatches.remove(event.getWatch());
            updateWatches(node, updatedWatches);
        });
    }

    private void updateWatches(EntityNode node, Set<Watch> updatedWatches) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        EntityNode updatedNode = EntityNode.get(
                node.getEntity(),
                node.getBrowserText(),
                node.getShortForms(),
                node.isDeprecated(),
                updatedWatches,
                node.getOpenCommentCount(),
                node.getTags());
        nodeIndex.updateNode(updatedNode);
    }

    private void handleCommentPosted(CommentPostedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        event.getEntity().ifPresent(entity -> {
            nodeIndex.getNode(entity.getEntity()).ifPresent(node -> {
                EntityNode updatedNode = EntityNode.get(
                        node.getEntity(),
                        node.getBrowserText(),
                        node.getShortForms(),
                        node.isDeprecated(),
                        node.getWatches(),
                        event.getOpenCommentCountForEntity(),
                        node.getTags());
                nodeIndex.updateNode(updatedNode);
            });
        });
    }

    private void handleDiscussionThreadStatusChanged(DiscussionThreadStatusChangedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        event.getEntity().ifPresent(entity -> {
            nodeIndex.getNode(entity).ifPresent(node -> {
                EntityNode updatedNode = EntityNode.get(
                        node.getEntity(),
                        node.getBrowserText(),
                        node.getShortForms(),
                        node.isDeprecated(),
                        node.getWatches(),
                        event.getOpenCommentsCountForEntity(),
                        node.getTags());
                nodeIndex.updateNode(updatedNode);
            });
        });
    }

    private void handleEntityDeprecatedChanged(EntityDeprecatedChangedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        nodeIndex.getNode(event.getEntity()).ifPresent(node -> {
            EntityNode updatedNode = EntityNode.get(
                    node.getEntity(),
                    node.getBrowserText(),
                    node.getShortForms(),
                    event.isDeprecated(),
                    node.getWatches(),
                    node.getOpenCommentCount(),
                    node.getTags());
            nodeIndex.updateNode(updatedNode);
        });
    }

    private void handleEntityTagsChanged(EntityTagsChangedEvent event) {
        if (nodeIndex == null) {
            throw createNodeIndexIsNullException();
        }
        nodeIndex.getNode(event.getEntity()).ifPresent(node -> {
            EntityNode updatedNode = EntityNode.get(
                    node.getEntity(),
                    node.getBrowserText(),
                    node.getShortForms(),
                    node.isDeprecated(),
                    node.getWatches(),
                    node.getOpenCommentCount(),
                    event.getTags());
            nodeIndex.updateNode(updatedNode);
        });
    }

    private static RuntimeException createNodeIndexIsNullException() {
        return new NullPointerException("NodeIndex is null");
    }

}

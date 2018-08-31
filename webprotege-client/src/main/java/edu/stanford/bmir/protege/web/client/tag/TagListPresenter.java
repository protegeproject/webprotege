package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.match.EntityCriteriaPresenter;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent.ON_ENTITY_TAGS_CHANGED;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2018
 *
 * Presents a list of tags for a given entity.
 */
public class TagListPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final TagListView view;

    @Nonnull
    private Optional<OWLEntity> currentEntity = Optional.empty();

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final Provider<TagView> tagViewProvider;

    @Inject
    public TagListPresenter(@Nonnull ProjectId projectId,
                            @Nonnull TagListView view,
                            @Nonnull DispatchServiceManager dispatchServiceManager,
                            @Nonnull Provider<TagView> tagViewProvider) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.tagViewProvider = checkNotNull(tagViewProvider);
    }

    /**
     * Starts this presenter.
     * @param container The container for the view presented by this presenter
     * @param eventBus The event bus used for listening to application and project
     *                 events.  Note that event handlers are automatically removed
     *                 by the caller.
     */
    public void start(@Nonnull AcceptsOneWidget container,
                      @Nonnull WebProtegeEventBus eventBus) {
        container.setWidget(view);
        eventBus.addProjectEventHandler(projectId,
                                        ON_ENTITY_TAGS_CHANGED,
                                        this::handleEntityTagsChanged);
    }

    /**
     * Sets the entity whose tags should be displayed by this presenter.
     * @param entity The entity.
     */
    public void setEntity(@Nonnull OWLEntity entity) {
        currentEntity = Optional.of(entity);
        dispatchServiceManager.execute(new GetEntityTagsAction(projectId,
                                                               entity),
                                       result -> setTags(result.getEntityTags()));
    }

    /**
     * Clears the entity that is being displayed by this presenter
     */
    public void clear() {
        currentEntity = Optional.empty();
        view.clear();
    }

    private void setTags(@Nonnull Collection<Tag> tags) {
        List<TagView> tagViews = tags.stream()
                                     .map(this::createTagViewForTag)
                                     .collect(toList());
        view.setTagViews(tagViews);
    }

    private TagView createTagViewForTag(@Nonnull Tag tag) {
        TagView tagView = tagViewProvider.get();
        tagView.setTagId(tag.getTagId());
        tagView.setLabel(tag.getLabel());
        tagView.setDescription(tag.getDescription());
        return tagView;
    }

    private void handleEntityTagsChanged(@Nonnull EntityTagsChangedEvent event) {
        if(currentEntity.equals(Optional.of(event.getEntity()))) {
            setTags(event.getTags());
        }
    }
}

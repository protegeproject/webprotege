package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.*;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2018
 */
public class EntityTagsSelectorPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityTagsSelectorView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nullable
    private OWLEntity entity;

    @Nonnull
    private final Set<Tag> selectedTags = new HashSet<>();

    @Inject
    public EntityTagsSelectorPresenter(@Nonnull ProjectId projectId,
                                       @Nonnull EntityTagsSelectorView view,
                                       @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void start(@Nonnull OWLEntity entity) {
        this.entity = entity;
        dispatchServiceManager.execute(new GetEntityTagsAction(projectId, entity),
                                       view,
                                       this::displayTags);
    }

    public void displayTags(GetEntityTagsResult result) {
        if(entity == null) {
            throw new RuntimeException("EntityTagsSelectorPresenter has not been started");
        }
        view.clear();
        selectedTags.clear();
        selectedTags.addAll(result.getEntityTags());
        result.getProjectTags().forEach(tag -> {
            EntityTagCheckBox checkBox = new EntityTagCheckBoxImpl();
            checkBox.setTag(tag);
            checkBox.setSelected(selectedTags.contains(tag));
            view.addCheckBox(checkBox);
        });
    }

    public void saveSelectedTags() {
        if(entity == null) {
            return;
        }
        List<Tag> tags = getSelectedTags();
        Set<TagId> fromTagIds = selectedTags.stream().map(Tag::getTagId).collect(toSet());
        Set<TagId> toTagIds = tags.stream().map(Tag::getTagId).collect(toSet());
        dispatchServiceManager.execute(new UpdateEntityTagsAction(projectId,
                                                                  entity,
                                                                  fromTagIds,
                                                                  toTagIds),
                                       result -> {});
    }

    @Nonnull
    public List<Tag> getSelectedTags() {
        return view.getCheckBoxes().stream()
                   .filter(EntityTagCheckBox::isSelected)
                   .map(EntityTagCheckBox::getTag)
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .collect(toList());
    }

    @Nonnull
    public EntityTagsSelectorView getView() {
        return view;
    }
}

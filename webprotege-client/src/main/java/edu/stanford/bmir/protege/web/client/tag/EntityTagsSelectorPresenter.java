package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsAction;
import edu.stanford.bmir.protege.web.shared.tag.GetEntityTagsResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

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

    @Inject
    public EntityTagsSelectorPresenter(@Nonnull ProjectId projectId,
                                       @Nonnull EntityTagsSelectorView view,
                                       @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.projectId = checkNotNull(projectId);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    public void start(@Nonnull OWLEntity entity) {
        dispatchServiceManager.execute(new GetEntityTagsAction(projectId, entity),
                                       view,
                                       this::displayTags);
    }

    public void displayTags(GetEntityTagsResult result) {
        view.clear();
        Set<Tag> selectedTags = new HashSet<>(result.getEntityTags());
        result.getProjectTags().forEach(tag -> {
            EntityTagCheckBox checkBox = new EntityTagCheckBoxImpl();
            checkBox.setTag(tag);
            checkBox.setSelected(selectedTags.contains(tag));
            view.addCheckBox(checkBox);
        });
    }

    public void saveSelectedTags() {
        List<Tag> tags = getSelectedTags();
        GWT.log("Save entity tags: " + tags);
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

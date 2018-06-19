package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagData;
import edu.stanford.bmir.protege.web.shared.tag.TagId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Mar 2018
 */
public interface ProjectTagsView extends IsWidget {

    interface ApplyChangesHandler {
        void handleApplyChanges();
    }

    interface CancelChangesHandler {
        void handleCancelChanges();
    }

    void setProjectTitle(@Nonnull String projectTitle);

    void setApplyChangesHandler(@Nonnull ApplyChangesHandler applyChangesHandler);

    void setCancelChangesHandler(@Nonnull CancelChangesHandler cancelChangesHandler);

    void setCancelButtonVisible(boolean visible);

    void setTags(@Nonnull List<Tag> tags, Map<TagId, Integer> usageCount);

    @Nonnull
    List<TagData> getTagData();

    void showDuplicateTagAlert(@Nonnull String label);

    @Nonnull
    AcceptsOneWidget getTagCriteriaContainer();
}

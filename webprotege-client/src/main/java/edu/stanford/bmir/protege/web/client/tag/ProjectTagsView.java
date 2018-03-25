package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagData;

import javax.annotation.Nonnull;
import java.util.List;

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

    void setTags(List<Tag> tags);

    List<TagData> getTagData();
}

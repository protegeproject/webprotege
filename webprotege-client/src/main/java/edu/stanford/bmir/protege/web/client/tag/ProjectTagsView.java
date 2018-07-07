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

    interface TagListChangedHandler {
        void handleTagListChanged();
    }

    void setTags(@Nonnull List<Tag> tags, Map<TagId, Integer> usageCount);

    void setTagListChangedHandler(@Nonnull TagListChangedHandler handler);

    @Nonnull
    List<TagData> getTagData();

    void showDuplicateTagAlert(@Nonnull String label);
}

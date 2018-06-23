package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public interface TagCriteriaView extends IsWidget {

    interface RemoveHandler {
        void handleRemove();
    }

    @Nonnull
    AcceptsOneWidget getTagCriteriaContainer();

    void setAvailableTagLabels(@Nonnull List<String> tagLabels);

    @Nonnull
    Optional<String> getSelectedTagLabel();

    void setSelectedTagLabel(@Nonnull String selectedTagLabel);

}

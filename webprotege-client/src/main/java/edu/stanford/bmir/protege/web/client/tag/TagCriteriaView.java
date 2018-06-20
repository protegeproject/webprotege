package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public interface TagCriteriaView extends IsWidget {

    @Nonnull
    AcceptsOneWidget getTagCriteriaContainer();

    void setAvailableTagNames(@Nonnull List<String> tagNames);

    Optional<String> getSelectedTagName();
}

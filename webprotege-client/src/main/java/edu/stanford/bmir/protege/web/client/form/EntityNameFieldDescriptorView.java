package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public interface EntityNameFieldDescriptorView extends IsWidget {

    void clear();

    @Nonnull
    LanguageMap getPlaceholder();

    void setPlaceholder(@Nonnull LanguageMap placeholder);

    @Nonnull
    AcceptsOneWidget getCriteriaViewContainer();
}

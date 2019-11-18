package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface FormDescriptorView extends IsWidget {

    @Nonnull
    String getFormId();

    void setFormId(@Nonnull String formId);

    @Nonnull
    LanguageMap getLabel();

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    AcceptsOneWidget getElementDescriptorListContainer();
}

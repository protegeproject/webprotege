package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public interface GridColumnDescriptorView extends IsWidget {

    void setId(@Nonnull GridColumnId id);

    @Nonnull
    GridColumnId getId();

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    LanguageMap getLabel();

    @Nonnull
    AcceptsOneWidget getBindingViewContainer();

    @Nonnull
    AcceptsOneWidget getFieldDescriptorChooserContainer();

}

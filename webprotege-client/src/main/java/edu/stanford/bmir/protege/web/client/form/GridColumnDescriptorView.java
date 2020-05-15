package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-26
 */
public interface GridColumnDescriptorView extends IsWidget, HasRequestFocus {

    interface LabelChangedHandler {
        void handleLabelChanged();
    }

    void setOptionality(Optionality optionality);

    Optionality getOptionality();

    @Nonnull
    Repeatability getRepeatability();

    void setRepeatability(@Nonnull Repeatability repeatability);

    void setLabel(@Nonnull LanguageMap label);

    void setLabelChangedHandler(@Nonnull LabelChangedHandler labelChangedHandler);

    @Nonnull
    LanguageMap getLabel();

    @Nonnull
    AcceptsOneWidget getBindingViewContainer();

    @Nonnull
    AcceptsOneWidget getFieldDescriptorChooserContainer();

}

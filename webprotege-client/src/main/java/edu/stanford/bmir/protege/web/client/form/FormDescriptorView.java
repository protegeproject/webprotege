package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public interface FormDescriptorView extends IsWidget {

    void clear();

    void setEnabled(boolean b);

    interface AddFormFieldHandler {
        void handleAddFormField();
    }

    @Nonnull
    LanguageMap getLabel();

    void setLabel(@Nonnull LanguageMap label);

    @Nonnull
    AcceptsOneWidget getFieldDescriptorListContainer();

    void setAddFormFieldHandler(@Nonnull AddFormFieldHandler handler);
}


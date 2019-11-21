package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public interface FormsManagerView extends IsWidget {

    void clear();

    void setFormIds(@Nonnull List<FormId> formIds);

    void setCurrentFormId(@Nonnull FormId formId);

    @Nonnull
    AcceptsOneWidget getFormDescriptorContainer();
}

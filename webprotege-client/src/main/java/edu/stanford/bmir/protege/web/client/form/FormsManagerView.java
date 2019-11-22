package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public interface FormsManagerView extends IsWidget {

    @Nonnull
    Optional<FormId> getCurrentFormId();

    interface AddFormHandler {
        void handleAddForm();
    }

    interface FormSelectionChangedHandler {
        void handleFormSelectionChanged();
    }

    void setFormSelectionChangedHandler(@Nonnull FormSelectionChangedHandler handler);

    interface FormIdEnteredHandler {
        void handleAcceptFormName(@Nonnull String formId);
    }

    void setAddFormHandler(@Nonnull AddFormHandler handler);

    void clear();

    void setFormIds(@Nonnull List<FormId> formIds);

    void addFormId(@Nonnull FormId formId);

    void setCurrentFormId(@Nonnull FormId formId);

    @Nonnull
    AcceptsOneWidget getFormDescriptorContainer();

    void displayCreateFormIdPrompt(FormIdEnteredHandler handler);


}

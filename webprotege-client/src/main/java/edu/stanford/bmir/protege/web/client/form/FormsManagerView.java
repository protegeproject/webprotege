package edu.stanford.bmir.protege.web.client.form;

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

    interface AddFormHandler {
        void handleAddForm();
    }

    interface FormSelectedHandler {
        void handleFormSelectionChanged(@Nonnull Optional<FormId> formId);
    }

    interface DeleteFormHandler {
        void handleDeleteForm(@Nonnull FormId formId);
    }

    interface EditFormHandler {
        void handleEditForm(@Nonnull FormId formId);
    }

    interface DeleteFormCallback {
        void deleteForm(@Nonnull FormId formId);
    }

    interface CopyFormsFromProjectHandler {
        void handleCopyFromsFromProject();
    }

    void setAddFormHandler(@Nonnull AddFormHandler handler);

    void setDeleteFormHandler(@Nonnull DeleteFormHandler handler);

    void setEditFormHandler(@Nonnull EditFormHandler handler);

    void setCopyFormsFromProjectHandler(@Nonnull CopyFormsFromProjectHandler handler);

    void setFormSelectedHandler(@Nonnull FormSelectedHandler handler);

    void clear();

    void setForms(List<FormIdPresenter> forms);

    void displayDeleteFormConfirmationMessage(@Nonnull String formName,
                                              @Nonnull FormId formId,
                                              @Nonnull DeleteFormCallback deleteFormCallback);
}

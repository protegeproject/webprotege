package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public interface FormsManagerView extends IsWidget {


    interface CopyFormsFromProjectHandler {
        void handleCopyFromsFromProject();
    }

    interface ExportFormsHandler {
        void handleExportForms();
    }

    interface ImportFormsHandler {
        void handleImportForms();
    }

    void setCopyFormsFromProjectHandler(@Nonnull CopyFormsFromProjectHandler handler);

    void setExportFormsHandler(@Nonnull ExportFormsHandler exportFormsHandler);

    void setImportFormsHandler(@Nonnull ImportFormsHandler importFormsHandler);

    void clear();

    void displayImportFormsInputBox(Consumer<String> importFormsJson);

    void displayImportFormsErrorMessage();

    @Nonnull
    AcceptsOneWidget getFormsListContainer();


}

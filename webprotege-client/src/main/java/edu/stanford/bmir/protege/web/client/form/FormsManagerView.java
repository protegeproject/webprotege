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

    interface CopyFormsFromProjectHandler {
        void handleCopyFromsFromProject();
    }

    void setCopyFormsFromProjectHandler(@Nonnull CopyFormsFromProjectHandler handler);

    void clear();

    @Nonnull
    AcceptsOneWidget getFormsListContainer();
}

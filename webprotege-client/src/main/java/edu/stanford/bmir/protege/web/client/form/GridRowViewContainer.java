package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

public interface GridRowViewContainer extends IsWidget, AcceptsOneWidget, HasRequestFocus, HasEnabled {

    interface DeleteHandler {
        void handleDelete();
    }

    void setDeleteHandler(@Nonnull DeleteHandler handler);

    void clear();
}

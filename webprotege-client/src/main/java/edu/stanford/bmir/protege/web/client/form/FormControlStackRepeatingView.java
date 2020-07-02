package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;

public interface FormControlStackRepeatingView extends IsWidget, HasEnabled {

    interface AddFormControlHandler {
        void handleAdd();
    }

    interface FormControlContainer extends AcceptsOneWidget, HasEnabled, HasRequestFocus, IsWidget {

        interface RemoveHandler {
            void handleRemove();
        }

        void setRemoveHandler(RemoveHandler handler);
    }

    void setAddFormControlHandler(@Nonnull AddFormControlHandler handler);

    @Nonnull
    FormControlContainer addFormControlContainer();

    void removeFormControlContainer(FormControlContainer container);

    void clear();

    @Nonnull
    AcceptsOneWidget getPaginatorContainer();

    void setPaginatorVisible(boolean visible);
}

package edu.stanford.bmir.protege.web.client.library.modal;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.AcceptKeyHandler;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 2018
 */
public interface ModalView extends IsWidget {


    void setCaption(@Nonnull String caption);

    /**
     * Sets the primary dialog button
     * @param button The button.
     * @param handler A handler that is run when the button is pressed.
     */
    void setPrimaryButton(@Nonnull DialogButton button, @Nonnull Runnable handler);

    /**
     * Sets the escape button.
     * @param button The button.
     * @param handler A handler that is run when the button is pressed.
     */
    void setEscapeButton(DialogButton button, @Nonnull Runnable handler);

    /**
     * Adds a button.
     * @param button The button.  The button will be styled as a regular dialog button, that is,
     *               neither an escape button nor an accept button.
     * @param handler A handler that is run when the button is pressed.
     */
    void addButton(@Nonnull DialogButton button, @Nonnull Runnable handler);

    void setContent(@Nonnull IsWidget content);

    void setPrimaryButtonFocusedOnAttach(boolean focused);

    void hide();
}

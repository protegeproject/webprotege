package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 * <p>
 *     Describes the content and appearance of a dialog.
 * </p>
 */
public abstract class WebProtegeDialogController<D> {

    private String title;
    
    private List<DialogButton> buttons;
    
    private DialogButton defaultButton;

    private DialogButton escapeButton;

    private List<WebProtegeDialogValidator> dialogValidators = new ArrayList<WebProtegeDialogValidator>();

    protected WebProtegeDialogController(String title, List<DialogButton> buttons, DialogButton defaultButton, DialogButton escapeButton) {
        this.title = title;
        this.buttons = new ArrayList<DialogButton>(buttons);
        this.defaultButton = defaultButton;
        this.escapeButton = escapeButton;
    }



    /**
     * Gets the dialog title. This is shown in the title bar of the dialog.
     * @return A string representing the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the buttons (e.g. OK, Cancel) that appear on the dialog.
     * @return A list of the buttons.
     */
    public List<DialogButton> getButtons() {
        return new ArrayList<DialogButton>(buttons);
    }

    /**
     * Gets the default button.  This button should appear in the list returned by {@link #getButtons()}.
     * @return The default button.  Not <code>null</code>.
     */
    public DialogButton getDefaultButton() {
        return defaultButton;
    }

    /**
     * Gets the button that dismisses the dialog without performing any action with the entered data.
     * This button should appear in the list returned by {@link #getButtons()}.
     * @return The cancel button.  Not <code>null</code>.
     */
    public DialogButton getEscapeButton() {
        return escapeButton;
    }

    /**
     * Gets the widget that is displayed in the dialog and allows information to be entered into the dialog.
     * @return The widget that the user interacts with.  Not <code>null</code>.
     */
    public abstract Widget getWidget();

    /**
     * Gets the focusable that should receive the focus when the dialog is shown.
     * @return The focusable that will receive the focus. Not <code>null</code>
     */
    public abstract Focusable getInitialFocusable();

    /**
     * Gets the installed validators for the contents of the widget provided by this controller.
     * @return The validator.  Not <code>null</code>.
     */
    public List<WebProtegeDialogValidator> getDialogValidators() {
        return new ArrayList<WebProtegeDialogValidator>(dialogValidators);
    }

    public void addDialogValidator(WebProtegeDialogValidator dialogValidator) {
        this.dialogValidators.add(dialogValidator);
    }

    public abstract D getData();

}

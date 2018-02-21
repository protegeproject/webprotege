package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/01/2012
 * <p>
 *     Describes the content and appearance of a settings.
 * </p>
 */
public abstract class WebProtegeDialogController<D> implements HasInitialFocusable {

    private String title;
    
    private final List<DialogButton> buttons;
    
    private DialogButton defaultButton;

    private DialogButton escapeButton;

    private final List<WebProtegeDialogValidator> dialogValidators = new ArrayList<>();

    private final Map<DialogButton, WebProtegeDialogButtonHandler<D>> buttonHandlers = new LinkedHashMap<>();

    protected WebProtegeDialogController(String title, List<DialogButton> buttons, DialogButton defaultButton, DialogButton escapeButton) {
        this.title = title;
        this.buttons = new ArrayList<>(buttons);
        this.defaultButton = defaultButton;
        this.escapeButton = escapeButton;
        initialiseHandlerList();
    }

    private void initialiseHandlerList() {
        for (DialogButton button : buttons) {
            buttonHandlers.put(button, new DefaultWebProtegeDialogButtonHandler<>());
        }
    }

    public void setDialogButtonHandler(DialogButton button, WebProtegeDialogButtonHandler<D> buttonHandler) {
        if(!buttonHandlers.containsKey(button)) {
            throw new IllegalArgumentException("Dialog does not contain button: " + button);
        }
        buttonHandlers.put(button, buttonHandler);
    }


    public Optional<WebProtegeDialogButtonHandler<D>> getButtonHandler(DialogButton button) {
        return Optional.ofNullable(buttonHandlers.get(button));
    }

    /**
     * Gets the settings title. This is shown in the title bar of the settings.
     * @return A string representing the title.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the buttons (e.g. OK, Cancel) that appear on the settings.
     * @return A list of the buttons.
     */
    public List<DialogButton> getButtons() {
        return new ArrayList<>(buttons);
    }

    /**
     * Gets the default button.  This button should appear in the list returned by {@link #getButtons()}.
     * @return The default button.  Not <code>null</code>.
     */
    public DialogButton getDefaultButton() {
        return defaultButton;
    }

    /**
     * Gets the button that dismisses the settings without performing any action with the entered data.
     * This button should appear in the list returned by {@link #getButtons()}.
     * @return The cancel button.  Not <code>null</code>.
     */
    public DialogButton getEscapeButton() {
        return escapeButton;
    }

    /**
     * Gets the widget that is displayed in the settings and allows information to be entered into the settings.
     * @return The widget that the user interacts with.  Not <code>null</code>.
     */
    public abstract Widget getWidget();

    /**
     * Gets the {@link Focusable} that should receive the focus when the settings is shown.  If no widget should receive the focus
     * then {@link Optional#empty()} will be returned.
     * @return The {@link Focusable} that will receive the focus. Not <code>null</code>
     */
    @Nonnull
    public abstract Optional<HasRequestFocus> getInitialFocusable();

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

package edu.stanford.bmir.protege.web.client.library.dlg;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import edu.stanford.bmir.protege.web.client.Messages;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public final class DialogButton {

    private static Messages messages = GWT.create(Messages.class);

    public static final DialogButton OK = new DialogButton((getMessages()).dialog_ok());

    public static final DialogButton CANCEL = new DialogButton(getMessages().dialog_cancel());

    public static final DialogButton YES = new DialogButton(getMessages().dialog_yes());

    public static final DialogButton NO = new DialogButton(getMessages().dialog_no());

    public static final DialogButton CREATE = new DialogButton(getMessages().create());

    public static final DialogButton DELETE = new DialogButton(getMessages().delete());

    public static final DialogButton CLOSE = new DialogButton(getMessages().dialog_close());

    public static final DialogButton SELECT = new DialogButton(getMessages().dialog_select());

    public static Messages getMessages() {
        return messages;
    }


    private String buttonName;

    private DialogButton(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
    
    public Button createButton() {
        return new Button(buttonName);
    }

    public static DialogButton get(@Nonnull String buttonName) {
        return new DialogButton(buttonName);
    }

    @Override
    public int hashCode() {
        return buttonName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DialogButton)) {
            return false;
        }
        DialogButton other = (DialogButton) obj;
        return this.buttonName.equals(other.buttonName);
    }


    @Override
    public String toString() {
        return toStringHelper("DialogButton")
                .addValue(buttonName)
                .toString();
    }
}

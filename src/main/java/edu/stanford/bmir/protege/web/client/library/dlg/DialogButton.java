package edu.stanford.bmir.protege.web.client.library.dlg;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import edu.stanford.bmir.protege.web.client.Messages;

import static edu.stanford.protege.widgetmap.client.view.ViewHolder.MESSAGES;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public enum DialogButton {

    OK((getMessages()).dialog_ok()),

    CANCEL(getMessages().dialog_cancel()),

    YES(getMessages().dialog_yes()),

    NO(getMessages().dialog_no());

    private static Messages getMessages() {
        return (Messages) GWT.create(Messages.class);
    }


    private String buttonName;

    DialogButton(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
    
    public Button createButton() {
        return new Button(buttonName);
    }
}

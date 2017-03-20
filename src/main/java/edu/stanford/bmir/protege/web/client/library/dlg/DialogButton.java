package edu.stanford.bmir.protege.web.client.library.dlg;


import com.google.gwt.user.client.ui.Button;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public enum DialogButton {
    
    OK("OK"),

    CANCEL("Cancel"),

    YES("Yes"),

    NO("No");
    
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

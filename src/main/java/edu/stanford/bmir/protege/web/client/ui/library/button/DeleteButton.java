package edu.stanford.bmir.protege.web.client.ui.library.button;

import com.google.gwt.user.client.ui.Button;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class DeleteButton extends Button {

    private static final String TEXT = "\u2716";


    public DeleteButton() {
        super(TEXT);
        addStyleName("button-style");
        addStyleName("delete-button");
        setTitle("Delete");
    }
}

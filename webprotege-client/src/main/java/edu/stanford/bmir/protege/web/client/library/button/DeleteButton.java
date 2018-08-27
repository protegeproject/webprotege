package edu.stanford.bmir.protege.web.client.library.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2012
 */
public class DeleteButton extends Button {

    private static final Messages MESSAGES =GWT.create(Messages.class);

    private static final String TEXT = "\u2715";


    public DeleteButton() {
        super();
        addStyleName(WebProtegeClientBundle.BUNDLE.buttons().deleteButton());
        setTitle(MESSAGES.delete());
    }
}

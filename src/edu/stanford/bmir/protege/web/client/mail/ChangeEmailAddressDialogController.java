package edu.stanford.bmir.protege.web.client.mail;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.user.EmailAddress;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/11/2013
 */
public class ChangeEmailAddressDialogController extends WebProtegeOKCancelDialogController<Optional<EmailAddress>> {

    private EmailAddressEditor emailAddressEditor;

    public ChangeEmailAddressDialogController() {
        super("Set Email Address");
        emailAddressEditor = new EmailAddressEditorImpl();
    }

    @Override
    public Widget getWidget() {
        return emailAddressEditor.asWidget();
    }

    public void setValue(EmailAddress emailAddress) {
        emailAddressEditor.setValue(emailAddress);
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return emailAddressEditor.getInitialFocusable();
    }

    @Override
    public Optional<EmailAddress> getData() {
        return emailAddressEditor.getValue();
    }
}

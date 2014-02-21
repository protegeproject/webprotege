package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.chgpwd.ChangePasswordData;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public class ChangePasswordDialogController extends WebProtegeOKCancelDialogController<ChangePasswordData> {

    private ChangePasswordView changePasswordView;

    public ChangePasswordDialogController(ChangePasswordView changePasswordView) {
        super("Change password");
        this.changePasswordView = changePasswordView;
    }

    @Override
    public Widget getWidget() {
        changePasswordView = new ChangePasswordViewImpl();
        return changePasswordView.asWidget();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return changePasswordView.getInitialFocusable();
    }

    @Override
    public ChangePasswordData getData() {
        return new ChangePasswordData(changePasswordView.getOldPassword(), changePasswordView.getNewPassword(), changePasswordView.getNewPasswordConfirmation());
    }
}

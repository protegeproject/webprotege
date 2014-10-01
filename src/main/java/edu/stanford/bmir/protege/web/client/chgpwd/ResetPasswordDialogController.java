package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordDialogController extends WebProtegeOKCancelDialogController<ResetPasswordData> {

    private ResetPasswordView view;

    public ResetPasswordDialogController(ResetPasswordView view) {
        super("Reset password");
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Override
    public Optional<Focusable> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public ResetPasswordData getData() {
        return view.getValue().get();
    }
}

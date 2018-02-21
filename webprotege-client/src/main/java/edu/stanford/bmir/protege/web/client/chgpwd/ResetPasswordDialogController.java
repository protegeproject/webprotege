package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogController;
import edu.stanford.bmir.protege.web.shared.chgpwd.ResetPasswordData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.client.library.dlg.DialogButton.CANCEL;
import static java.util.Arrays.asList;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordDialogController extends WebProtegeDialogController<ResetPasswordData> {

    private ResetPasswordView view;

    @Inject
    public ResetPasswordDialogController(ResetPasswordView view, Messages messages) {
        super("Reset password",
              asList(CANCEL, DialogButton.get(messages.password_resetPassword())),
              CANCEL,
              DialogButton.get(messages.password_resetPassword()));
        this.view = view;
    }

    @Override
    public Widget getWidget() {
        return view.asWidget();
    }

    @Nonnull
    @Override
    public Optional<HasRequestFocus> getInitialFocusable() {
        return view.getInitialFocusable();
    }

    @Override
    public ResetPasswordData getData() {
        return view.getValue().get();
    }
}

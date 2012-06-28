package edu.stanford.bmir.protege.web.client.ui.signup;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.SignupInfo;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeOKCancelDialogController;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class WebProtegeSignupDialogController extends WebProtegeOKCancelDialogController<SignupInfo> {

    public static final String TITLE = "Create an Account";

    private final WebProtegeSignupDialogForm form;

    public WebProtegeSignupDialogController() {
        super(TITLE);
        form = new WebProtegeSignupDialogForm();
        addDialogValidator(form);
    }

    @Override
    public Widget getWidget() {
        return form;
    }

    @Override
    public Focusable getInitialFocusable() {
        return form.getInitialFocusable();
    }

    @Override
    public SignupInfo getData() {
        return form.getData();
    }
}

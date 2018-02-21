package edu.stanford.bmir.protege.web.client.chgpwd;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public interface ChangePasswordView extends IsWidget, HasInitialFocusable {

    String getOldPassword();

    String getNewPassword();

    String getNewPasswordConfirmation();

    void clear();
}

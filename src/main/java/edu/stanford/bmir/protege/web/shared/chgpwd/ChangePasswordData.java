package edu.stanford.bmir.protege.web.shared.chgpwd;

import com.google.gwt.user.client.rpc.IsSerializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2013
 */
public final class ChangePasswordData implements IsSerializable {

    private String oldPassword;

    private String newPassword;

    private String newPasswordConfirmation;

    /**
     * For serialization purposes only
     */
    private ChangePasswordData() {
    }

    public ChangePasswordData(String oldPassword, String newPassword, String newPasswordConfirmation) {
        this.oldPassword = checkNotNull(oldPassword);
        this.newPassword = checkNotNull(newPassword);
        this.newPasswordConfirmation = checkNotNull(newPasswordConfirmation);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewPasswordConfirmation() {
        return newPasswordConfirmation;
    }

    public boolean isNewPasswordConfirmed() {
        return newPassword.equals(newPasswordConfirmation);
    }
}

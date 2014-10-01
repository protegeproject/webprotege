package edu.stanford.bmir.protege.web.shared.chgpwd;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordAction implements Action<ResetPasswordResult> {

    private ResetPasswordData resetPasswordData;

    /**
     * For serialization purposes only
     */
    private ResetPasswordAction() {
    }

    public ResetPasswordAction(ResetPasswordData resetPasswordData) {
        this.resetPasswordData = checkNotNull(resetPasswordData);
    }

    public ResetPasswordData getResetPasswordData() {
        return resetPasswordData;
    }

    @Override
    public int hashCode() {
        return "ResetPasswordAction".hashCode() + resetPasswordData.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ResetPasswordAction)) {
            return false;
        }
        ResetPasswordAction other = (ResetPasswordAction) o;
        return this.resetPasswordData.equals(other.resetPasswordData);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ResetPasswordAction").addValue(resetPasswordData).toString();
    }
}

package edu.stanford.bmir.protege.web.shared.chgpwd;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/10/2014
 */
public class ResetPasswordResult implements Result {

    private ResetPasswordResultCode resultCode;

    /**
     * For serialization purposes only
     */
    private ResetPasswordResult() {
    }

    public ResetPasswordResult(ResetPasswordResultCode resultCode) {
        this.resultCode = checkNotNull(resultCode);
    }

    public ResetPasswordResultCode getResultCode() {
        return resultCode;
    }


    @Override
    public int hashCode() {
        return "ResetPasswordResult".hashCode() + resultCode.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof ResetPasswordResult)) {
            return false;
        }
        ResetPasswordResult other = (ResetPasswordResult) o;
        return this.resultCode == other.resultCode;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("ResetPasswordResult").addValue(resultCode).toString();
    }
}

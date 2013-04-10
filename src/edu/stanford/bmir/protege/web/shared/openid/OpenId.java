package edu.stanford.bmir.protege.web.shared.openid;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class OpenId implements Serializable {

    private String openId;

    private OpenId() {
    }


    private OpenId(String openId) {
        this.openId = checkNotNull(openId);
    }

    /**
     * Creates a fresh open id.
     * @param openId The name of the open id.  Not {@code null}.
     * @return An open id for the specified name.
     * @throws NullPointerException if {@code openId} is {@code null}.
     */
    public static OpenId getOpenId(String openId) {
        return new OpenId(openId);
    }

    /**
     * Gets the name of this open id.
     * @return The name.  Not {@code null}.
     */
    public String getOpenId() {
        return openId;
    }

    @Override
    public int hashCode() {
        return "OpenId".hashCode() + openId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OpenId)) {
            return false;
        }
        OpenId other = (OpenId) obj;
        return this.openId.equals(other.openId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OpenId");
        sb.append("(");
        sb.append(openId);
        sb.append(")");
        return sb.toString();
    }
}

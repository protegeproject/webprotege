package edu.stanford.bmir.protege.web.shared.user;


import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class EmailAddress implements Serializable {

    private String emailAddress;

    private EmailAddress() {
    }

    public EmailAddress(String emailAddress) {
        if(emailAddress == null) {
            throw new NullPointerException("emailAddress must not be null");
        }
        this.emailAddress = emailAddress;
    }

    public boolean isEmpty() {
        return emailAddress.isEmpty();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public int hashCode() {
        return emailAddress.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EmailAddress)) {
            return false;
        }
        EmailAddress other = (EmailAddress) obj;
        return emailAddress.equals(other.emailAddress);
    }


    @Override
    public String toString() {
        return toStringHelper("EmailAddress")
                .addValue(emailAddress)
                .toString();
    }
}

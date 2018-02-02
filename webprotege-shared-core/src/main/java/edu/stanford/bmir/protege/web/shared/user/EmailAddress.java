package edu.stanford.bmir.protege.web.shared.user;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class EmailAddress implements Serializable {

    private String address;

    @GwtSerializationConstructor
    private EmailAddress() {
    }

    @JsonCreator
    public EmailAddress(@Nonnull String address) {
        checkNotNull(address);
        this.address = address;
    }

    public boolean isEmpty() {
        return address.isEmpty();
    }

    @Nonnull
    @JsonValue
    public String getEmailAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
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
        return address.equals(other.address);
    }


    @Override
    public String toString() {
        return toStringHelper("EmailAddress")
                .addValue(address)
                .toString();
    }
}

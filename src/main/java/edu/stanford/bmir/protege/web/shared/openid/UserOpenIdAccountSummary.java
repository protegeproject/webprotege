package edu.stanford.bmir.protege.web.shared.openid;

import edu.stanford.bmir.protege.web.shared.user.UserId;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class UserOpenIdAccountSummary implements Serializable {

    private UserId userId;

    private Set<OpenIdAccountDetails> accountDetails;

    private UserOpenIdAccountSummary() {
    }

    public UserOpenIdAccountSummary(UserId userId, Set<OpenIdAccountDetails> accountDetails) {
        this.userId = checkNotNull(userId);
        this.accountDetails = new HashSet<OpenIdAccountDetails>(checkNotNull(accountDetails));
    }

    public static UserOpenIdAccountSummary empty(UserId userId) {
        return new UserOpenIdAccountSummary(userId, Collections.<OpenIdAccountDetails>emptySet());
    }

    public UserId getUserId() {
        return userId;
    }

    public Set<OpenIdAccountDetails> getAccountDetails() {
        return new HashSet<OpenIdAccountDetails>(accountDetails);
    }

    public int getNumberOfAccounts() {
        return accountDetails.size();
    }
}

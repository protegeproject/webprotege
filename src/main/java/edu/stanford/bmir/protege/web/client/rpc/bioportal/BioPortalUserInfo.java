package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalUserInfo implements Serializable {

    private String accountName;
    
    private String firstName;
    
    private String lastName;

    private BioPortalUserId userId;
    
    private String email;

    /**
     * Default constructor for serialization purposes
     */
    private BioPortalUserInfo() {

    }

    public BioPortalUserInfo(String accountName, String firstName, String lastName, BioPortalUserId userId, String email) {
        this.accountName = accountName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BioPortalUserId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "BioPortalUserInfo(accountName(" + accountName + ") firstName(" + firstName + ") lastName(" + lastName + ") userId(" + userId + ") email(" + email + "))";
    }
}

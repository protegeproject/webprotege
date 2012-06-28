package edu.stanford.bmir.protege.web.client.ui.ontology.accesspolicy.domain;


import java.io.Serializable;

/**
 * Contains data for user invitation
 *
 * @author z.khan
 *
 */
public class Invitation implements Serializable {

    private String emailId;

    private boolean isWriter; // If true then its Writer else if false then its a Reader.

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }

    public boolean isWriter() {
        return isWriter;
    }

}


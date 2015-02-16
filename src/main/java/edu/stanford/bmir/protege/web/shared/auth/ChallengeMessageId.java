package edu.stanford.bmir.protege.web.shared.auth;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/02/15
 */
public class ChallengeMessageId implements IsSerializable {

    private String id;

    private ChallengeMessageId() {
    }

    public ChallengeMessageId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}

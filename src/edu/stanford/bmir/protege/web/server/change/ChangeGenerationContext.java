package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class ChangeGenerationContext {

    private UserId userId;

    public ChangeGenerationContext(UserId userId) {
        this.userId = checkNotNull(userId);
    }



    public UserId getUserId() {
        return userId;
    }
}

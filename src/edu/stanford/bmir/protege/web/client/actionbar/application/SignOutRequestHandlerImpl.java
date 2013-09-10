package edu.stanford.bmir.protege.web.client.actionbar.application;

import edu.stanford.bmir.protege.web.client.Application;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public class SignOutRequestHandlerImpl implements SignOutRequestHandler {

    @Override
    public void handleSignOutRequest() {
        Application.get().doLogOut();
    }
}

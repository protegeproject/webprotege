package edu.stanford.bmir.protege.web.client.model.listener;

import edu.stanford.bmir.protege.web.client.model.event.UpdateShareLinkEvent;

public interface ShareOntologyAccessListener {

    void updateShareLink(UpdateShareLinkEvent projectChangedEvent);

    void updateShareLink(boolean showShareLink);

    void onUserLoggedInUpdateShareLink();

    void closeShareAccessWindowOnSignOut();
}


package edu.stanford.bmir.protege.web.server.owlapi.change;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class RevisionStoreProvider implements Provider<RevisionStore> {

    private RevisionStoreImpl revisionStore;

    private boolean loaded = false;

    @Inject
    public RevisionStoreProvider(RevisionStoreImpl revisionStore) {
        this.revisionStore = revisionStore;
    }

    @Override
    public synchronized RevisionStore get() {
        if(!loaded) {
            revisionStore.load();
            loaded = true;
        }
        return revisionStore;
    }
}

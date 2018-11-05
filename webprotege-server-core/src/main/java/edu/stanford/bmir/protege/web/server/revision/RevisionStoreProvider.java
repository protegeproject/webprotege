package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.project.ProjectDisposablesManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class RevisionStoreProvider implements Provider<RevisionStore> {

    @Nonnull
    private final RevisionStoreImpl revisionStore;

    @Nonnull
    private final ProjectDisposablesManager disposablesManager;

    private boolean loaded = false;

    @Inject
    public RevisionStoreProvider(@Nonnull RevisionStoreImpl revisionStore,
                                 @Nonnull ProjectDisposablesManager disposablesManager) {
        this.revisionStore = checkNotNull(revisionStore);
        this.disposablesManager = checkNotNull(disposablesManager);
    }

    @Override
    public synchronized RevisionStore get() {
        if(!loaded) {
            revisionStore.load();
            loaded = true;
            disposablesManager.register(revisionStore);
        }
        return revisionStore;
    }
}

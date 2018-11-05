package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Nov 2018
 */
@ApplicationSingleton
public class ApplicationDisposablesManager {

    @Nonnull
    private final DisposableObjectManager disposableObjectManager;

    @Inject
    public ApplicationDisposablesManager(@Nonnull DisposableObjectManager disposableObjectManager) {
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
    }

    public void register(@Nonnull HasDispose disposable) {
        disposableObjectManager.register(disposable);
    }

    public void dispose() {
        disposableObjectManager.dispose();
    }
}

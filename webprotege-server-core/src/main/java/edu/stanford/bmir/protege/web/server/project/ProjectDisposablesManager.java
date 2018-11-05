package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.util.DisposableObjectManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Nov 2018
 */
@ProjectSingleton
public class ProjectDisposablesManager {

    @Nonnull
    private final DisposableObjectManager disposableObjectManager;

    @Inject
    public ProjectDisposablesManager(@Nonnull DisposableObjectManager disposableObjectManager) {
        this.disposableObjectManager = checkNotNull(disposableObjectManager);
    }

    public void register(@Nonnull HasDispose dispose) {
        disposableObjectManager.register(dispose);
    }

    public void dispose() {
        disposableObjectManager.dispose();
    }
}

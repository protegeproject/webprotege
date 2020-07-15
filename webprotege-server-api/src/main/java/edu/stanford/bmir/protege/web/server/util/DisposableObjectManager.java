package edu.stanford.bmir.protege.web.server.util;

import edu.stanford.bmir.protege.web.shared.HasDispose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.LinkedHashSet;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Apr 2018
 */
public class DisposableObjectManager {

    private static final Logger logger = LoggerFactory.getLogger(DisposableObjectManager.class);

    private final LinkedHashSet<HasDispose> disposables = new LinkedHashSet<>();

    @Inject
    public DisposableObjectManager() {
    }

    public void register(@Nonnull HasDispose disposable) {
        disposables.add(checkNotNull(disposable));
    }

    public synchronized void dispose() {
        disposables.forEach(disposable -> {
            try {
                disposable.dispose();
            }
            catch (Throwable throwable) {
                logger.error("An error occurred whilst disposing of object", throwable);
            }
        });
        disposables.clear();
    }
}

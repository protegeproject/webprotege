package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.server.admin.ApplicationSettingsManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Mar 2017
 */
public class ApplicationPortSupplier {

    private final ApplicationSettingsManager manager;

    @Inject
    public ApplicationPortSupplier(@Nonnull ApplicationSettingsManager manager) {
        this.manager = checkNotNull(manager);
    }

    public Optional<Integer> getApplicationPort() {
        int port = manager.getApplicationSettings().getApplicationLocation().getPort();
        if(port != 0) {
            return Optional.of(
                    port
            );
        }
        else {
            return Optional.empty();
        }
    }
}

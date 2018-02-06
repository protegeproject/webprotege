package edu.stanford.bmir.protege.web.server.app;

import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 20 Dec 2017
 */
public class ApplicationSettingsChecker {

    private static Logger logger = LoggerFactory.getLogger(ApplicationSettingsChecker.class);

    @Nonnull
    private final ApplicationSettings settings;

    @Nonnull
    private PlaceUrl placeUrl;

    @Inject
    public ApplicationSettingsChecker(@Nonnull ApplicationSettings applicationSettings,
                                       @Nonnull PlaceUrl placeUrl) {
        this.settings = checkNotNull(applicationSettings);
        this.placeUrl = checkNotNull(placeUrl);
    }

    /**
     * Checks to see whether the application settings is properly configured.
     * @return true if the application settings is properly configured, otherwise false.
     */
    public boolean isProperlyConfigured() {
        return getConfigurationErrors().isEmpty();
    }

    /**
     * Gets a list of configuration error messages.
     * @return A list of configuration error messages.  If there are no configuration errors
     * then the list will be empty.
     */
    @Nonnull
    public List<String> getConfigurationErrors() {
        List<String> configurationErrors = new ArrayList<>();
        if(settings.getApplicationName().isEmpty()) {
            configurationErrors.add("Application name is not specified");
        }
        if(settings.getSystemNotificationEmailAddress().isEmpty()) {
            configurationErrors.add("System notification email address is not specified");
        }
        ApplicationLocation location = settings.getApplicationLocation();
        if(location.getHost().trim().isEmpty()) {
            configurationErrors.add("Application host is not specified");
        }
        if(location.getScheme().trim().isEmpty()) {
            configurationErrors.add("Application scheme (http(s)) is not specified");
        }
        try {
            placeUrl.getApplicationUrl();
        } catch (Exception e) {
            configurationErrors.add(String.format("Cannot construct well-formed application URL. (%s)", e.getMessage()));
        }
        try {
            placeUrl.getProjectUrl(ProjectId.get(UUID.randomUUID().toString()));
        } catch (Exception e) {
            configurationErrors.add(String.format("Cannot construct well-formed project URLs. (%s)", e.getMessage()));
        }
        try {
            placeUrl.getEntityUrl(ProjectId.get(UUID.randomUUID().toString()),
                                  DataFactory.getOWLThing());
        } catch (Exception e) {
            configurationErrors.add(String.format("Cannot construct well-formed OWL Entity URLs. (%s)", e.getMessage()));
        }
        configurationErrors.forEach(e -> logger.warn(e));
        return configurationErrors;
    }
}

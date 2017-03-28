package edu.stanford.bmir.protege.web.server.templates;


import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.app.ApplicationSettings;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2017
 */
@SuppressWarnings("ResultOfMethodCallIgnored" )
public class TemplateObjectsBuilder {

    public static final String PROJECT_ID = "project.id";

    public static final String PROJECT_DISPLAY_NAME = "project.displayName";

    public static final String PROJECT_URL = "project.url";

    public static final String APPLICATION_NAME = "application.name";

    public static final String APPLICATION_URL = "application.url";

    public static final String APPLICATION_LOCATION_SCHEME = "application.location.scheme";

    public static final String APPLICATION_LOCATION_HOST = "application.location.host";

    public static final String APPLICATION_LOCATION_PATH = "application.location.path";

    public static final String APPLICATION_LOCATION_PORT = "application.location.port";

    private final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

    public static TemplateObjectsBuilder builder() {
        return new TemplateObjectsBuilder();
    }

    public TemplateObjectsBuilder withUserId(UserId userId) {
        builder.put("userId", userId.getUserName());
        return this;
    }

    public TemplateObjectsBuilder withEntity(OWLEntityData entityData) {
        builder.put("entity", entityData.getEntity());
        builder.put("entity.browserText", entityData.getBrowserText());
        return this;
    }

    public TemplateObjectsBuilder withProjectId(@Nonnull ProjectId projectId) {
        builder.put(PROJECT_ID, projectId.getId());
        return this;
    }

    public TemplateObjectsBuilder withProjectDisplayName(@Nonnull String displayName) {
        builder.put(PROJECT_DISPLAY_NAME, displayName);
        return this;
    }

    public TemplateObjectsBuilder withProjectUrl(@Nonnull String projectUrl) {
        builder.put(PROJECT_URL, projectUrl);
        return this;
    }

    public TemplateObjectsBuilder withProjectDetails(@Nonnull ProjectDetails projectDetails) {
        builder.put(PROJECT_ID, projectDetails.getProjectId().getId());
        builder.put(PROJECT_DISPLAY_NAME, projectDetails.getDisplayName());
        return this;
    }

    public TemplateObjectsBuilder withApplicationName(@Nonnull String applicationName) {
        builder.put(APPLICATION_NAME, applicationName);
        return this;
    }

    public TemplateObjectsBuilder withApplicationUrl(@Nonnull String url) {
        builder.put(APPLICATION_URL, url);
        return this;
    }

    public TemplateObjectsBuilder withApplicationSettings(ApplicationSettings settings) {
        builder.put(APPLICATION_NAME, settings.getApplicationName());
        builder.put(APPLICATION_LOCATION_SCHEME, settings.getApplicationLocation().getScheme());
        builder.put(APPLICATION_LOCATION_HOST, settings.getApplicationLocation().getHost());
        builder.put(APPLICATION_LOCATION_PATH, settings.getApplicationLocation().getPath());
        builder.put(APPLICATION_LOCATION_PORT, settings.getApplicationLocation().getPort());
        return this;
    }

    public TemplateObjectsBuilder with(@Nonnull String name, @Nonnull Object object) {
        builder.put(name, object);
        return this;
    }

    public ImmutableMap<String, Object> build() {
        return builder.build();
    }

}

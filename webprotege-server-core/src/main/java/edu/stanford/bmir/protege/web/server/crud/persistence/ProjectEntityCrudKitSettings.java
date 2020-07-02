package edu.stanford.bmir.protege.web.server.crud.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 * <p>
 *     Captures the {@link EntityCrudKitSettings} for a project.
 * </p>
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectEntityCrudKitSettings {


    public static final String PROJECT_ID = "_id";

    public static final String SETTINGS = "settings";

    /**
     * Constructs a {@link ProjectEntityCrudKitSettings}, which specifies the {@link EntityCrudKitSettings} for a
     * specified project.
     *
     * @param projectId The {@link ProjectId} that identifies the settings that apply to the project.  Not {@code null}.
     * @param settings  The settings that apply to the project.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    @JsonCreator
    public static ProjectEntityCrudKitSettings get(@JsonProperty(PROJECT_ID) @Nonnull ProjectId projectId,
                                                   @JsonProperty(
                                                           SETTINGS) @Nonnull EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> settings) {

        return new AutoValue_ProjectEntityCrudKitSettings(projectId, settings);
    }

    /**
     * Gets the {@link ProjectId}, which specifies the project that these settings apply to.
     *
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @JsonProperty(PROJECT_ID)
    public abstract ProjectId getProjectId();

    /**
     * Gets the {@link EntityCrudKitSettings} for the project.
     *
     * @return The settings.  Not {@code null}.
     */
    @JsonProperty(SETTINGS)
    public abstract EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> getSettings();
}

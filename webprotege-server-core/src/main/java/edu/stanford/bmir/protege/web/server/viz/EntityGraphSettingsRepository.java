package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.viz.EntityGraphSettings;
import edu.stanford.bmir.protege.web.shared.viz.ProjectUserEntityGraphSettings;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public interface EntityGraphSettingsRepository extends Repository {

    void saveSettings(@Nonnull ProjectUserEntityGraphSettings settings);

    @Nonnull
    ProjectUserEntityGraphSettings getProjectDefaultSettings(@Nonnull ProjectId projectId);

    /**
     * Gets the entity graph settings, or the project default if a user does
     * not have specific settings associated with them.
     * @param projectId The project Id
     * @param userId The user Id
     * @return The settings
     */
    @Nonnull
    ProjectUserEntityGraphSettings getSettingsForUserOrProjectDefault(@Nonnull ProjectId projectId,
                                                                      @Nonnull UserId userId);


}

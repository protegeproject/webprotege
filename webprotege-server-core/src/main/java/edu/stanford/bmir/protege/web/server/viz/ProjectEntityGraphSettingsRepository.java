package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.persistence.Repository;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
public interface ProjectEntityGraphSettingsRepository extends Repository {

    void saveSettings(@Nonnull ProjectEntityGraphSettings settings);

    @Nonnull
    ProjectEntityGraphSettings getSettings(@Nonnull ProjectId projectId);

}

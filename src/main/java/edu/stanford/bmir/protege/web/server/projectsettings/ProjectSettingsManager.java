package edu.stanford.bmir.protege.web.server.projectsettings;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/11/14
 */
public interface ProjectSettingsManager {

    void setProjectSettings(ProjectSettings projectSettings);

    ProjectSettings getProjectSettings(ProjectId projectId) throws UnknownProjectException;

}

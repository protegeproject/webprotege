package edu.stanford.bmir.protege.web.client.sharing;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public interface SharingSettingsPresenterFactory {

    SharingSettingsPresenter getPresenter(ProjectId projectId);
}

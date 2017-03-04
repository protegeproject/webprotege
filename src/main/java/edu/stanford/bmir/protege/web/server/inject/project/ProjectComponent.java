package edu.stanford.bmir.protege.web.server.inject.project;

import dagger.Subcomponent;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Oct 2016
 */
@Subcomponent(
        modules = {
            ProjectModule.class
        }
)
@ProjectSingleton
public interface ProjectComponent {

    OWLAPIProject getProject();
}

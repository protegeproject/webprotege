package edu.stanford.bmir.protege.web.client.project;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class ActiveProjectIdProvider implements Provider<ProjectId> {

    @Override
    public ProjectId get() {
        // This doesn't seem right.
        return Application.get().getActiveProject().get();
    }
}

package edu.stanford.bmir.protege.web.client.inject;

import edu.stanford.bmir.protege.web.client.project.ActiveProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2014
 */
public class ActiveProjectIdProvider implements Provider<ProjectId> {

    private ActiveProjectManager activeProjectManager;

    @Inject
    public ActiveProjectIdProvider(ActiveProjectManager activeProjectManager) {
        this.activeProjectManager = activeProjectManager;
    }

    @Override
    public ProjectId get() {
        Optional<ProjectId> activeProjectId = activeProjectManager.getActiveProjectId();
        if(activeProjectId.isPresent()) {
            return activeProjectId.get();
        }
        else {
            throw new RuntimeException("Active project Id is not present.  Cannot provide it.");
        }
    }
}

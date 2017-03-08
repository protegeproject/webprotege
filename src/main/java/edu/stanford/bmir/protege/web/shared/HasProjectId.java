package edu.stanford.bmir.protege.web.shared;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
public interface HasProjectId {

    /**
     * Get the {@link ProjectId}.
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    ProjectId getProjectId();
}

package edu.stanford.bmir.protege.web.client.ui;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 * <p>
 *     An interface to objects that provide the means to display a project.
 * </p>
 */
public interface ProjectDisplay extends HasProjectId {

    /**
     * Gets the id of the displayed project.
     * @return The {@link ProjectId} of the displayed project.  Not {@code null}.
     */
    ProjectId getProjectId();
}

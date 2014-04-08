package edu.stanford.bmir.protege.web.client.ui.ontology.home;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 *
 * <p>
 *     A display that can accept info required to create a new project.
 * </p>
 */
public interface NewProjectInfoDisplay {

    /**
     * Gets the name of the project that is currently entered or selected.
     * @return A string representing the name of the project.
     */
    public String getProjectName();

    NewProjectSettings getNewProjectInfo();
}

package edu.stanford.bmir.protege.web.shared.irigen.action;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2013
 */
public class GetIRIGeneratorSettingsAction extends AbstractHasProjectAction<GetIRIGeneratorSettingsResult> {

    /**
     * For serialization purposes only
     */
    private GetIRIGeneratorSettingsAction() {
    }

    public GetIRIGeneratorSettingsAction(ProjectId projectId) {
        super(projectId);
    }

}

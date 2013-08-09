package edu.stanford.bmir.protege.web.shared.irigen.action;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.irigen.IRIGeneratorSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/08/2013
 */
public class SetIRIGeneratorSettingsAction extends AbstractHasProjectAction<SetIRIGeneratorSettingsResult> {

    private IRIGeneratorSettings settings;

    /**
     * For serialization only
     */
    private SetIRIGeneratorSettingsAction() {
    }

    public SetIRIGeneratorSettingsAction(ProjectId projectId, IRIGeneratorSettings settings) {
        super(projectId);
        this.settings = settings;
    }

    public IRIGeneratorSettings getSettings() {
        return settings;
    }
}

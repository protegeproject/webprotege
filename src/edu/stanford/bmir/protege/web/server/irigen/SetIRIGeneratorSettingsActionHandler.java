package edu.stanford.bmir.protege.web.server.irigen;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.irigen.action.SetIRIGeneratorSettingsAction;
import edu.stanford.bmir.protege.web.shared.irigen.action.SetIRIGeneratorSettingsResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/08/2013
 */
public class SetIRIGeneratorSettingsActionHandler extends AbstractHasProjectActionHandler<SetIRIGeneratorSettingsAction, SetIRIGeneratorSettingsResult> {

    @Override
    public Class<SetIRIGeneratorSettingsAction> getActionClass() {
        return SetIRIGeneratorSettingsAction.class;
    }

    @Override
    protected RequestValidator<SetIRIGeneratorSettingsAction> getAdditionalRequestValidator(SetIRIGeneratorSettingsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected SetIRIGeneratorSettingsResult execute(SetIRIGeneratorSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
//        project.setIRIGeneratorSettings(action.getSuffixSettings());
        return new SetIRIGeneratorSettingsResult();
    }
}

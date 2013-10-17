package edu.stanford.bmir.protege.web.server.irigen;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.irigen.action.GetIRIGeneratorSettingsAction;
import edu.stanford.bmir.protege.web.shared.irigen.action.GetIRIGeneratorSettingsResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 31/07/2013
 */
public class GetIRIGeneratorSettingsActionHandler extends AbstractHasProjectActionHandler<GetIRIGeneratorSettingsAction, GetIRIGeneratorSettingsResult> {

    @Override
    public Class<GetIRIGeneratorSettingsAction> getActionClass() {
        return GetIRIGeneratorSettingsAction.class;
    }

    @Override
    protected RequestValidator<GetIRIGeneratorSettingsAction> getAdditionalRequestValidator(GetIRIGeneratorSettingsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected GetIRIGeneratorSettingsResult execute(GetIRIGeneratorSettingsAction action, OWLAPIProject project, ExecutionContext executionContext) {
//        IRIGeneratorSettings settings = project.getIRIGeneratorSettings();
        return new GetIRIGeneratorSettingsResult(null);
    }
}

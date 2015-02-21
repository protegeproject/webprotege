package edu.stanford.bmir.protege.web.server.revision;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberAction;
import edu.stanford.bmir.protege.web.shared.revision.GetHeadRevisionNumberResult;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetHeadRevisionNumberActionHandler extends AbstractHasProjectActionHandler<GetHeadRevisionNumberAction, GetHeadRevisionNumberResult> {

    @Override
    public Class<GetHeadRevisionNumberAction> getActionClass() {
        return GetHeadRevisionNumberAction.class;
    }

    @Override
    protected RequestValidator<GetHeadRevisionNumberAction> getAdditionalRequestValidator(GetHeadRevisionNumberAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetHeadRevisionNumberResult execute(GetHeadRevisionNumberAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetHeadRevisionNumberResult(project.getRevisionNumber());
    }
}

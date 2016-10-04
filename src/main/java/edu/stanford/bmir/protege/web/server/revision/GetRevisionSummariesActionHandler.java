package edu.stanford.bmir.protege.web.server.revision;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesAction;
import edu.stanford.bmir.protege.web.shared.revision.GetRevisionSummariesResult;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class GetRevisionSummariesActionHandler extends AbstractHasProjectActionHandler<GetRevisionSummariesAction, GetRevisionSummariesResult> {

    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Inject
    public GetRevisionSummariesActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetRevisionSummariesAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetRevisionSummariesResult execute(GetRevisionSummariesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new GetRevisionSummariesResult(ImmutableList.copyOf(project.getChangeManager().getRevisionSummaries()));
    }

    @Override
    public Class<GetRevisionSummariesAction> getActionClass() {
        return GetRevisionSummariesAction.class;
    }
}

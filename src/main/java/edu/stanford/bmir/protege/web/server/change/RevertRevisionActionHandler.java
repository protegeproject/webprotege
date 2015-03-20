package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionAction;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevertRevisionActionHandler extends AbstractProjectChangeHandler<OWLEntity, RevertRevisionAction, RevertRevisionResult> {

    private Provider<OWLOntologyChangeDataReverter> reverterProvider;

    @Inject
    public RevertRevisionActionHandler(OWLAPIProjectManager projectManager, Provider<OWLOntologyChangeDataReverter> reverterProvider) {
        super(projectManager);
        this.reverterProvider = reverterProvider;
    }


    @Override
    public Class<RevertRevisionAction> getActionClass() {
        return RevertRevisionAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(RevertRevisionAction action, OWLAPIProject project, ExecutionContext executionContext) {
        RevisionNumber revisionNumber = action.getRevisionNumber();
        return new RevisionReverterChangeListGenerator(revisionNumber, reverterProvider.get());
    }

    @Override
    protected RevertRevisionResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult, RevertRevisionAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new RevertRevisionResult(project.getProjectId(), eventList);
    }

    @Override
    protected RequestValidator<RevertRevisionAction> getAdditionalRequestValidator(RevertRevisionAction action, RequestContext requestContext) {
        return UserHasProjectWritePermissionValidator.get();
    }

    @Override
    protected ChangeDescriptionGenerator<OWLEntity> getChangeDescription(RevertRevisionAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>("Reverted the changes in Revision " + action.getRevisionNumber().getValue());
    }
}

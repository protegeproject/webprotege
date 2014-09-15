package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.crud.ChangeSetEntityCrudSession;
import edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.crud.EntityShortForm;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractUpdateFrameHandler<A extends UpdateFrameAction<F, S>, F extends EntityFrame<S>,  S extends OWLEntity> extends AbstractHasProjectActionHandler<A, Result> implements ActionHandler<A, Result> {


    /**
     * Gets an additional validator that is specific to the implementing handler.  This is returned as part of a
     * {@link edu.stanford.bmir.protege.web.server.dispatch.validators.CompositeRequestValidator} by the the implementation
     * of
     * the {@link #getRequestValidator(edu.stanford.bmir.protege.web.shared.dispatch.Action,
     * edu.stanford.bmir.protege.web.server.dispatch.RequestContext)} method.
     * @param action The action that the validation will be completed against.
     * @param requestContext The {@link edu.stanford.bmir.protege.web.server.dispatch.RequestContext} that describes the
     * context for the request.
     * @return A {@link edu.stanford.bmir.protege.web.server.dispatch.RequestValidator} for this handler.  Not {@code
     *         null}.
     */
    @Override
    protected RequestValidator<A> getAdditionalRequestValidator(A action, RequestContext requestContext) {
        return UserHasProjectWritePermissionValidator.get();
    }

    /**
     * Executes the specified action, against the specified project in the specified context.
     * @param action The action to be handled/executed
     * @param project The project that the action should be executed with respect to.
     * @param executionContext The {@link edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext} that should be
     * used to provide details such as the
     * {@link edu.stanford.bmir.protege.web.shared.user.UserId} of the user who requested the action be executed.
     * @return The result of the execution to be returned to the client.
     */
    @Override
    protected Result execute(A action, OWLAPIProject project, ExecutionContext executionContext) {
        LabelledFrame<F> from = action.getFrom();
        LabelledFrame<F> to = action.getTo();
        final EventTag startTag = project.getEventManager().getCurrentTag();
        if(from.equals(to)) {
            return createResponse(action.getTo(), project.getEventManager().getEventsFromTag(startTag));
        }

        UserId userId = executionContext.getUserId();

        FrameTranslator<F, S> translator = createTranslator();


        final FrameChangeGenerator<F, S> changeGenerator = new FrameChangeGenerator<F, S>(from.getFrame(), to.getFrame(), translator);
        final FixedMessageChangeDescriptionGenerator<S> descGenerator = new FixedMessageChangeDescriptionGenerator<S>(getChangeDescription(from, to));
        project.applyChanges(userId, changeGenerator, descGenerator);
        if(!from.getDisplayName().equals(to.getDisplayName())) {
            // Set changes

            EntityCrudKitHandler<?, ChangeSetEntityCrudSession> entityEditorKit = project.getEntityCrudKitHandler();
            ChangeSetEntityCrudSession session = entityEditorKit.createChangeSetSession();
            OntologyChangeList.Builder<S> changeListBuilder = new OntologyChangeList.Builder<S>();
            entityEditorKit.update(session, to.getFrame().getSubject(),
                                     EntityShortForm.get(to.getDisplayName()),
                                     project.getEntityCrudContext(executionContext.getUserId()),
                                     changeListBuilder);
            FixedChangeListGenerator<S> changeListGenerator = FixedChangeListGenerator.get(changeListBuilder.build().getChanges());
            FixedMessageChangeDescriptionGenerator<S> changeDescriptionGenerator = FixedMessageChangeDescriptionGenerator.get("Renamed entity");
            project.applyChanges(userId, changeListGenerator, changeDescriptionGenerator);
        }
        EventList<ProjectEvent<?>> events = project.getEventManager().getEventsFromTag(startTag);
        return createResponse(action.getTo(), events);
    }

    protected abstract Result createResponse(LabelledFrame<F> to, EventList<ProjectEvent<?>> events);

    protected abstract FrameTranslator<F, S> createTranslator();

    protected abstract String getChangeDescription(LabelledFrame<F> from, LabelledFrame<F> to);
}

package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.dispatch.actions.UpdateFrameAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nullable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractUpdateFrameHandler<A extends UpdateFrameAction<F, S>, F extends EntityFrame<S>,  S extends OWLEntity> extends AbstractHasProjectActionHandler<A, Result> implements ActionHandler<A, Result> {

    public AbstractUpdateFrameHandler(ProjectManager projectManager,
                                      AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
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

        final FrameChangeGenerator<F, S> changeGenerator = new FrameChangeGenerator<>(from.getFrame(), to.getFrame(), translator);
        ChangeDescriptionGenerator<S> generator = project.getChangeDescriptionGeneratorFactory().get("Edited " + from.getDisplayName());
        project.applyChanges(userId, changeGenerator, generator);
        EventList<ProjectEvent<?>> events = project.getEventManager().getEventsFromTag(startTag);
        return createResponse(action.getTo(), events);
    }

//    private void applyChangesToChangeDisplayName(OWLAPIProject project, ExecutionContext executionContext, LabelledFrame<F> from, LabelledFrame<F> to, UserId userId) {
//        // Set changes
//        EntityCrudKitHandler<?, ChangeSetEntityCrudSession> entityEditorKit = project.getEntityCrudKitHandler();
//        ChangeSetEntityCrudSession session = entityEditorKit.createChangeSetSession();
//        OntologyChangeList.Builder<S> changeListBuilder = new OntologyChangeList.Builder<>();
//        entityEditorKit.update(session, to.getFrame().getSubject(),
//                                 EntityShortForm.get(to.getTitle()),
//                                 project.getEntityCrudContext(executionContext.getUserId()),
//                                 changeListBuilder);
//        FixedChangeListGenerator<S> changeListGenerator = FixedChangeListGenerator.get(changeListBuilder.build().getChanges());
//        String typePrintName = from.getFrame().getSubject().getEntityType().getPrintName().toLowerCase();
//        FixedMessageChangeDescriptionGenerator<S> changeDescriptionGenerator =
//                FixedMessageChangeDescriptionGenerator.get("Renamed the " + typePrintName + " " + from.getTitle() + " to " + to.getTitle());
//        project.applyChanges(userId, changeListGenerator, changeDescriptionGenerator);
//    }

    protected abstract Result createResponse(LabelledFrame<F> to, EventList<ProjectEvent<?>> events);

    protected abstract FrameTranslator<F, S> createTranslator();

    protected abstract String getChangeDescription(LabelledFrame<F> from, LabelledFrame<F> to);
}

package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionAction;
import edu.stanford.bmir.protege.web.shared.change.RevertRevisionResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
public class RevertRevisionActionHandler extends AbstractProjectChangeHandler<OWLEntity, RevertRevisionAction, RevertRevisionResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Provider<OWLOntologyChangeDataReverter> reverterProvider;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public RevertRevisionActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull Provider<OWLOntologyChangeDataReverter> reverterProvider,
                                       @Nonnull RevisionManager revisionManager,
                                       @Nonnull OWLOntology rootOntology) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = projectId;
        this.reverterProvider = reverterProvider;
        this.revisionManager = revisionManager;
        this.rootOntology = rootOntology;
    }

    @Override
    public Class<RevertRevisionAction> getActionClass() {
        return RevertRevisionAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(RevertRevisionAction action,
                                                                    ExecutionContext executionContext) {
        RevisionNumber revisionNumber = action.getRevisionNumber();
        return new RevisionReverterChangeListGenerator(revisionNumber,
                                                       reverterProvider.get(),
                                                       revisionManager,
                                                       rootOntology);
    }

    @Override
    protected RevertRevisionResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                      RevertRevisionAction action,
                                                      ExecutionContext executionContext,
                                                      EventList<ProjectEvent<?>> eventList) {
        return new RevertRevisionResult(projectId, eventList);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.REVERT_CHANGES;
    }

    @Override
    protected ChangeDescriptionGenerator<OWLEntity> getChangeDescription(RevertRevisionAction action,
                                                                         ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>("Reverted the changes in Revision " + action.getRevisionNumber().getValue());
    }
}

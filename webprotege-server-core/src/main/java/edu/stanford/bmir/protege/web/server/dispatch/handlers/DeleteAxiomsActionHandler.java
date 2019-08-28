package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.DeleteAxiomsResult;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.project.chg.ChangeManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Apr 2018
 */
public class DeleteAxiomsActionHandler extends AbstractProjectActionHandler<DeleteAxiomsAction, DeleteAxiomsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public DeleteAxiomsActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull ChangeManager changeManager,
                                     @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.changeManager = checkNotNull(changeManager);
        this.defaultOntologyIdManager = checkNotNull(defaultOntologyIdManager);
    }

    @Nonnull
    @Override
    public Class<DeleteAxiomsAction> getActionClass() {
        return DeleteAxiomsAction.class;
    }

    @Nonnull
    @Override
    public DeleteAxiomsResult execute(@Nonnull DeleteAxiomsAction action, @Nonnull ExecutionContext executionContext) {
        var builder = OntologyChangeList.<String>builder();
        var ontId = defaultOntologyIdManager.getDefaultOntologyId();
        action.getAxioms()
              .forEach(ax -> builder.add(RemoveAxiomChange.of(ontId, ax)));
        var changeList = builder.build(action.getCommitMessage());
        var changeListGenerator = new FixedChangeListGenerator<>(changeList.getChanges(),
                                                                 "",
                                                                 action.getCommitMessage());
        var result = changeManager.applyChanges(executionContext.getUserId(),
                                                changeListGenerator);
        int removedAxioms = result.getChangeList()
                                  .size();
        return new DeleteAxiomsResult(projectId, removedAxioms);
    }
}

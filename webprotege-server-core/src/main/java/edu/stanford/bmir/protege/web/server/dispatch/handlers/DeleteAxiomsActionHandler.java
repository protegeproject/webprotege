package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsResult;
import edu.stanford.bmir.protege.web.server.dispatch.actions.DeleteAxiomsAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.DeleteAxiomsResult;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;

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
    private final OWLOntology rootOntology;

    @Inject
    public DeleteAxiomsActionHandler(@Nonnull AccessManager accessManager,
                                     @Nonnull ProjectId projectId,
                                     @Nonnull ChangeManager changeManager,
                                     @Nonnull OWLOntology rootOntology) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.changeManager = checkNotNull(changeManager);
        this.rootOntology = checkNotNull(rootOntology);
    }

    @Nonnull
    @Override
    public Class<DeleteAxiomsAction> getActionClass() {
        return DeleteAxiomsAction.class;
    }

    @Nonnull
    @Override
    public DeleteAxiomsResult execute(@Nonnull DeleteAxiomsAction action, @Nonnull ExecutionContext executionContext) {
        OntologyChangeList.Builder<String> builder = OntologyChangeList.builder();
        action.getAxioms().forEach(ax -> builder.removeAxiom(rootOntology, ax));
        OntologyChangeList<String> changeList = builder.build(action.getCommitMessage());
        FixedChangeListGenerator<String> changeListGenerator = new FixedChangeListGenerator<>(changeList.getChanges(),
                                                                                              "",
                                                                                              action.getCommitMessage());
        ChangeApplicationResult<String> result = changeManager.applyChanges(executionContext.getUserId(),
                                                                            changeListGenerator);
        int removedAxioms = result.getChangeList().size();
        return new DeleteAxiomsResult(projectId, removedAxioms);
    }
}

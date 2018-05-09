package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsResult;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2018
 */
public class AddAxiomsActionHandler extends AbstractProjectActionHandler<AddAxiomsAction, AddAxiomsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public AddAxiomsActionHandler(@Nonnull AccessManager accessManager,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull ChangeManager changeManager,
                                  @Nonnull OWLOntology rootOntology) {
        super(accessManager);
        this.projectId = projectId;
        this.changeManager = changeManager;
        this.rootOntology = rootOntology;
    }

    @Nonnull
    @Override
    public Class<AddAxiomsAction> getActionClass() {
        return AddAxiomsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public AddAxiomsResult execute(@Nonnull AddAxiomsAction action, @Nonnull ExecutionContext executionContext) {
        OntologyChangeList.Builder<String> builder = OntologyChangeList.builder();
        action.getAxioms().forEach(ax -> builder.addAxiom(rootOntology, ax));
        OntologyChangeList<String> changeList = builder.build(action.getCommitMessage());
        FixedChangeListGenerator<String> changeListGenerator = new FixedChangeListGenerator<>(changeList.getChanges(),
                                                                                              "",
                                                                                              action.getCommitMessage());
        ChangeApplicationResult<String> result = changeManager.applyChanges(executionContext.getUserId(),
                                                                                                   changeListGenerator);
        int addedAxiomsCount = result.getChangeList().size();
        return new AddAxiomsResult(projectId, addedAxiomsCount);
    }
}

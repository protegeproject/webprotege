package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsResult;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.project.chg.ChangeManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

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
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public AddAxiomsActionHandler(@Nonnull AccessManager accessManager,
                                  @Nonnull ProjectId projectId,
                                  @Nonnull ChangeManager changeManager,
                                  @Nonnull DefaultOntologyIdManager defaultOntologyIdManager) {
        super(accessManager);
        this.projectId = projectId;
        this.changeManager = changeManager;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    @Nonnull
    @Override
    public Class<AddAxiomsAction> getActionClass() {
        return AddAxiomsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(AddAxiomsAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public AddAxiomsResult execute(@Nonnull AddAxiomsAction action, @Nonnull ExecutionContext executionContext) {
        var builder = OntologyChangeList.<String>builder();
        var ontId = defaultOntologyIdManager.getDefaultOntologyId();
        action.getAxioms()
              .forEach(ax -> builder.add(AddAxiomChange.of(ontId, ax)));
        var changeList = builder.build(action.getCommitMessage());
        var changeListGenerator = new FixedChangeListGenerator<>(changeList.getChanges(),
                                                                 "",
                                                                 action.getCommitMessage());
        var result = changeManager.applyChanges(executionContext.getUserId(),
                                                changeListGenerator);
        int addedAxiomsCount = result.getChangeList()
                                     .size();
        return new AddAxiomsResult(projectId, addedAxiomsCount);
    }
}

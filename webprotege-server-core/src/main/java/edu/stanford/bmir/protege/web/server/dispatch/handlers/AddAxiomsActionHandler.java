package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsAction;
import edu.stanford.bmir.protege.web.server.dispatch.actions.AddAxiomsResult;
import org.semanticweb.owlapi.model.OWLAxiom;
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

    private final ChangeManager changeManager;

    private final OWLOntology rootOntology;

    @Inject
    public AddAxiomsActionHandler(@Nonnull AccessManager accessManager, ChangeManager changeManager, OWLOntology rootOntology) {
        super(accessManager);
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
        // Preliminary implementation for testing
        OntologyChangeList.Builder<String> builder = OntologyChangeList.builder();
        for(OWLAxiom ax : action.getAxioms()) {
            builder.addAxiom(rootOntology, ax);
        }
        OntologyChangeList<String> test = builder.build("");
        changeManager.applyChanges(executionContext.getUserId(),
                                   new FixedChangeListGenerator<>(test.getChanges(), "", action.getCommitMessage()));
        return new AddAxiomsResult();
    }
}

package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsActionHandler extends AbstractProjectActionHandler<CreateNamedIndividualsAction, CreateNamedIndividualsResult> {

    @Nonnull
    private final HasApplyChanges changeApplicator;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Nonnull
    private final CreateIndividualsChangeListGeneratorFactory factory;

    @Inject
    public CreateNamedIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull HasApplyChanges changeApplicator,
                                               @Nonnull EntityNodeRenderer renderer,
                                               @Nonnull CreateIndividualsChangeListGeneratorFactory factory) {
        super(accessManager);
        this.changeApplicator = changeApplicator;
        this.renderer = renderer;
        this.factory = factory;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(EDIT_ONTOLOGY, CREATE_INDIVIDUAL);
    }

    @Nonnull
    @Override
    public CreateNamedIndividualsResult execute(@Nonnull CreateNamedIndividualsAction action,
                                                @Nonnull ExecutionContext executionContext) {
        ChangeApplicationResult<Set<OWLNamedIndividual>> result = changeApplicator.applyChanges(executionContext.getUserId(),
                                                                                                factory.create(action.getType(),
                                                                                                               action.getSourceText()));
        Set<EntityNode> individualData = result.getSubject().stream()
                                               .map(renderer::render)
                                               .collect(toSet());
        return new CreateNamedIndividualsResult(individualData);
    }

    @Nonnull
    @Override
    public Class<CreateNamedIndividualsAction> getActionClass() {
        return CreateNamedIndividualsAction.class;
    }
}

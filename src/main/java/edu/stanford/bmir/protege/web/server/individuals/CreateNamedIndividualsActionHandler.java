package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsActionHandler extends AbstractHasProjectActionHandler<CreateNamedIndividualsAction, CreateNamedIndividualsResult> {

    @Nonnull
    private final HasApplyChanges changeApplicator;

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateNamedIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull HasApplyChanges changeApplicator,
                                               @Nonnull RenderingManager renderer,
                                               @Nonnull @RootOntology OWLOntology rootOntology,
                                               @Nonnull OWLDataFactory dataFactory) {
        super(accessManager);
        this.changeApplicator = changeApplicator;
        this.renderer = renderer;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(EDIT_ONTOLOGY, CREATE_INDIVIDUAL);
    }

    @Override
    public CreateNamedIndividualsResult execute(CreateNamedIndividualsAction action,
                                                ExecutionContext executionContext) {
        ChangeApplicationResult<Set<OWLNamedIndividual>> result = changeApplicator.applyChanges(executionContext.getUserId(),
                                                                                                new CreateIndividualsChangeListGenerator(
                                                                                                        action.getShortNames(),
                                                                                                        action.getType(),
                                                                                                        rootOntology,
                                                                                                        dataFactory),
                                                                                                new FixedMessageChangeDescriptionGenerator<>(
                                                                                                        "Created individuals"));
        Set<OWLNamedIndividualData> individualData = result.getSubject().orElse(emptySet()).stream()
                                                           .map(renderer::getRendering)
                                                           .collect(toSet());
        return new CreateNamedIndividualsResult(individualData);
    }

    @Override
    public Class<CreateNamedIndividualsAction> getActionClass() {
        return CreateNamedIndividualsAction.class;
    }
}

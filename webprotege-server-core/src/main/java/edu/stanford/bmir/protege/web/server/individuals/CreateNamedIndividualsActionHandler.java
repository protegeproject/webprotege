package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateNamedIndividualsResult;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.EventTag;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_INDIVIDUAL;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class CreateNamedIndividualsActionHandler extends AbstractProjectActionHandler<CreateNamedIndividualsAction, CreateNamedIndividualsResult> {

    @Nonnull
    private final ProjectId projectId;

    private final EventManager<ProjectEvent<?>> eventManager;

    @Nonnull
    private final HasApplyChanges changeApplicator;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Nonnull
    private final CreateIndividualsChangeListGeneratorFactory factory;

    @Inject
    public CreateNamedIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull ProjectId projectId, EventManager<ProjectEvent<?>> eventManager, @Nonnull HasApplyChanges changeApplicator,
                                               @Nonnull EntityNodeRenderer renderer,
                                               @Nonnull CreateIndividualsChangeListGeneratorFactory factory) {
        super(accessManager);
        this.projectId = checkNotNull(projectId);
        this.eventManager = checkNotNull(eventManager);
        this.changeApplicator = checkNotNull(changeApplicator);
        this.renderer = checkNotNull(renderer);
        this.factory = checkNotNull(factory);
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(CreateNamedIndividualsAction action) {
        return Arrays.asList(EDIT_ONTOLOGY, CREATE_INDIVIDUAL);
    }

    @Nonnull
    @Override
    public CreateNamedIndividualsResult execute(@Nonnull CreateNamedIndividualsAction action,
                                                @Nonnull ExecutionContext executionContext) {
        EventTag eventTag = eventManager.getCurrentTag();
        ChangeApplicationResult<Set<OWLNamedIndividual>> result = changeApplicator.applyChanges(executionContext.getUserId(),
                                                                                                factory.create(action.getTypes(),
                                                                                                               action.getSourceText(),
                                                                                                               action.getLangTag()));
        EventList<ProjectEvent<?>> eventList = eventManager.getEventsFromTag(eventTag);
        ImmutableSet<EntityNode> individualData = result.getSubject().stream()
                                                        .map(renderer::render)
                                                        .collect(toImmutableSet());
        return new CreateNamedIndividualsResult(projectId,
                                                eventList,
                                                individualData);
    }

    @Nonnull
    @Override
    public Class<CreateNamedIndividualsAction> getActionClass() {
        return CreateNamedIndividualsAction.class;
    }
}

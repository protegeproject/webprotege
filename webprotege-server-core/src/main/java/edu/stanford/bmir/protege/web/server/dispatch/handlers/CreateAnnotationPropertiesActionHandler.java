package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateAnnotationPropertiesChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateAnnotationPropertiesResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_PROPERTY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateAnnotationPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLAnnotationProperty>, CreateAnnotationPropertiesAction, CreateAnnotationPropertiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CreateAnnotationPropertiesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Inject
    public CreateAnnotationPropertiesActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                   @Nonnull HasApplyChanges applyChanges,
                                                   @Nonnull ProjectId projectId,
                                                   @Nonnull RenderingManager renderer,
                                                   @Nonnull CreateAnnotationPropertiesChangeGeneratorFactory changeGeneratorFactory, @Nonnull EntityNodeRenderer entityNodeRenderer) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = checkNotNull(projectId);
        this.changeGeneratorFactory = checkNotNull(changeGeneratorFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
    }

    @Override
    protected ChangeListGenerator<Set<OWLAnnotationProperty>> getChangeListGenerator(CreateAnnotationPropertiesAction action,
                                                                                     ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.getSourceText(),
                                             action.getLangTag(),
                                             action.getParents());
    }

    @Override
    protected CreateAnnotationPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLAnnotationProperty>> changeApplicationResult,
                                                                  CreateAnnotationPropertiesAction action,
                                                                  ExecutionContext executionContext,
                                                                  EventList<ProjectEvent<?>> eventList) {
        Set<OWLAnnotationProperty> properties = changeApplicationResult.getSubject();
        return new CreateAnnotationPropertiesResult(projectId,
                                                    properties.stream()
                                                          .map(entityNodeRenderer::render)
                                                          .collect(toImmutableSet()),
                                                    eventList);
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(CreateAnnotationPropertiesAction action) {
        return asList(EDIT_ONTOLOGY, CREATE_PROPERTY);
    }

    @Nonnull
    @Override
    public Class<CreateAnnotationPropertiesAction> getActionClass() {
        return CreateAnnotationPropertiesAction.class;
    }
}

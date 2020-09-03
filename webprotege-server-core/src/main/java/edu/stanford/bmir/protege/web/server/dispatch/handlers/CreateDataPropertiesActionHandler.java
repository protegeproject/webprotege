package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateDataPropertiesChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDataPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateDataPropertiesResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_PROPERTY;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 25/03/2013
 */
public class CreateDataPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLDataProperty>, CreateDataPropertiesAction, CreateDataPropertiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CreateDataPropertiesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Inject
    public CreateDataPropertiesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull CreateDataPropertiesChangeGeneratorFactory changeGeneratorFactory, @Nonnull EntityNodeRenderer entityNodeRenderer) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = checkNotNull(projectId);
        this.changeGeneratorFactory = checkNotNull(changeGeneratorFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
    }

    @Override
    protected ChangeListGenerator<Set<OWLDataProperty>> getChangeListGenerator(CreateDataPropertiesAction action,
                                                                               ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.getSourceText(),
                                             action.getLangTag(),
                                             action.getParents());
    }

    @Override
    protected CreateDataPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLDataProperty>> changeApplicationResult,
                                                            CreateDataPropertiesAction action,
                                                            ExecutionContext executionContext,
                                                            EventList<ProjectEvent<?>> eventList) {
        Map<OWLDataProperty, String> map = new HashMap<>();
        Set<OWLDataProperty> properties = changeApplicationResult.getSubject();
        return new CreateDataPropertiesResult(projectId,
                                              properties.stream()
                                                    .map(entityNodeRenderer::render)
                                                    .collect(toImmutableSet()),
                                              eventList);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(CreateDataPropertiesAction action) {
        return CREATE_PROPERTY;
    }

    @Nonnull
    @Override
    public Class<CreateDataPropertiesAction> getActionClass() {
        return CreateDataPropertiesAction.class;
    }
}

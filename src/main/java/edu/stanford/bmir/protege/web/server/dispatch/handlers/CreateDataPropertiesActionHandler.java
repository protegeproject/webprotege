package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateDataPropertiesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateDataPropertiesResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_PROPERTY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateDataPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLDataProperty>, CreateDataPropertiesAction, CreateDataPropertiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateDataPropertiesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull ProjectId projectId,
                                             @Nonnull RenderingManager renderingManager,
                                             @Nonnull @RootOntology OWLOntology rootOntology,
                                             @Nonnull OWLDataFactory dataFactory) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    protected ChangeListGenerator<Set<OWLDataProperty>> getChangeListGenerator(CreateDataPropertiesAction action,
                                                                               ExecutionContext executionContext) {
        return new CreateDataPropertiesChangeGenerator(action.getBrowserTexts(),
                                                       action.getParent(),
                                                       rootOntology,
                                                       dataFactory);
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLDataProperty>> getChangeDescription(CreateDataPropertiesAction action,
                                                                                    ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>(action.getBrowserTexts().size() == 1 ?
                                                                    String.format("Created data property %s as a sub-property of %s",
                                                                                  action.getBrowserTexts().iterator().next(),
                                                                                  action.getParent()
                                                                                        .map(renderingManager::getBrowserText)
                                                                                        .orElse("owl:topDataProperty"))
                                                                    :
                                                                    "Created data properties");
    }

    @Override
    protected CreateDataPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLDataProperty>> changeApplicationResult,
                                                            CreateDataPropertiesAction action,
                                                            ExecutionContext executionContext,
                                                            EventList<ProjectEvent<?>> eventList) {
        Map<OWLDataProperty, String> map = new HashMap<>();
        for(OWLDataProperty dataProperty : changeApplicationResult.getSubject().get()) {
            map.put(dataProperty, renderingManager.getBrowserText(dataProperty));
        }
        return new CreateDataPropertiesResult(map, projectId, action.getParent(), eventList);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return CREATE_PROPERTY;
    }

    @Override
    public Class<CreateDataPropertiesAction> getActionClass() {
        return CreateDataPropertiesAction.class;
    }
}

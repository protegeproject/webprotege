package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateAnnotationPropertiesResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private final RenderingManager renderer;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateAnnotationPropertiesActionHandler(@Nonnull AccessManager accessManager,
                                                   @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                   @Nonnull HasApplyChanges applyChanges,
                                                   @Nonnull ProjectId projectId,
                                                   @Nonnull RenderingManager renderer,
                                                   @Nonnull OWLOntology rootOntology,
                                                   @Nonnull OWLDataFactory dataFactory) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = projectId;
        this.renderer = renderer;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    protected ChangeListGenerator<Set<OWLAnnotationProperty>> getChangeListGenerator(CreateAnnotationPropertiesAction action,
                                                                                     ExecutionContext executionContext) {
        return new CreateAnnotationPropertiesChangeGenerator(action.getBrowserTexts(),
                                                             action.getParent(),
                                                             rootOntology,
                                                             dataFactory);
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLAnnotationProperty>> getChangeDescription(
            CreateAnnotationPropertiesAction action,
            ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>("Created annotation properties");
    }

    @Override
    protected CreateAnnotationPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLAnnotationProperty>> changeApplicationResult,
                                                                  CreateAnnotationPropertiesAction action,
                                                                  ExecutionContext executionContext,
                                                                  EventList<ProjectEvent<?>> eventList) {
        Map<OWLAnnotationProperty, String> map = new HashMap<>();
        for(OWLAnnotationProperty result : changeApplicationResult.getSubject().get()) {
            map.put(result, renderer.getBrowserText(result));
        }
        return new CreateAnnotationPropertiesResult(map, projectId, action.getParent(), eventList);
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return asList(EDIT_ONTOLOGY, CREATE_PROPERTY);
    }

    @Override
    public Class<CreateAnnotationPropertiesAction> getActionClass() {
        return CreateAnnotationPropertiesAction.class;
    }
}

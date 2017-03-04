package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateAnnotationPropertiesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateAnnotationPropertiesResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

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

    @Inject
    public CreateAnnotationPropertiesActionHandler(ProjectManager projectManager,
                                                   AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    protected ChangeListGenerator<Set<OWLAnnotationProperty>> getChangeListGenerator(CreateAnnotationPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new CreateAnnotationPropertiesChangeGenerator(action.getBrowserTexts(), action.getParent());
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLAnnotationProperty>> getChangeDescription(CreateAnnotationPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLAnnotationProperty>>("Created annotation properties");
    }

    @Override
    protected CreateAnnotationPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLAnnotationProperty>> changeApplicationResult, CreateAnnotationPropertiesAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Map<OWLAnnotationProperty, String> map = new HashMap<OWLAnnotationProperty, String>();
        for(OWLAnnotationProperty result : changeApplicationResult.getSubject().get()) {
            map.put(result, project.getRenderingManager().getBrowserText(result));
        }
        return new CreateAnnotationPropertiesResult(map, project.getProjectId(), action.getParent(), eventList);
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

package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.dispatch.validators.WritePermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveOntologyAnnotation;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
public class SetOntologyAnnotationsActionHandler extends AbstractProjectChangeHandler<Set<OWLAnnotation>, SetOntologyAnnotationsAction, SetOntologyAnnotationsResult> {

    private final ValidatorFactory<WritePermissionValidator> validatorFactory;

    @Inject
    public SetOntologyAnnotationsActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<WritePermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<SetOntologyAnnotationsAction> getActionClass() {
        return SetOntologyAnnotationsAction.class;
    }

    @Override
    protected ChangeListGenerator<Set<OWLAnnotation>> getChangeListGenerator(SetOntologyAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        final Set<OWLAnnotation> fromAnnotations = action.getFromAnnotations();
        final Set<OWLAnnotation> toAnnotations = action.getToAnnotations();

        List<OWLOntologyChange> changeList = new ArrayList<OWLOntologyChange>();

        for(OWLAnnotation annotation : fromAnnotations) {
            if(!toAnnotations.contains(annotation)) {
                changeList.add(new RemoveOntologyAnnotation(project.getRootOntology(), annotation));
            }
        }
        for(OWLAnnotation annotation : toAnnotations) {
            if(!fromAnnotations.contains(annotation)) {
                changeList.add(new AddOntologyAnnotation(project.getRootOntology(), annotation));
            }
        }
        return new FixedChangeListGenerator<Set<OWLAnnotation>>(changeList) {
            @Override
            public Set<OWLAnnotation> getRenamedResult(Set<OWLAnnotation> result, RenameMap renameMap) {
                return super.getRenamedResult(result, renameMap);
            }
        };
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLAnnotation>> getChangeDescription(SetOntologyAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLAnnotation>>("Edited ontology annotations");
    }

    @Override
    protected SetOntologyAnnotationsResult createActionResult(ChangeApplicationResult<Set<OWLAnnotation>> changeApplicationResult, SetOntologyAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new SetOntologyAnnotationsResult(project.getRootOntology().getAnnotations(), eventList);
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(SetOntologyAnnotationsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }
}

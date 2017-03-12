package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveOntologyAnnotation;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY_ANNOTATIONS;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
public class SetOntologyAnnotationsActionHandler extends AbstractProjectChangeHandler<Set<OWLAnnotation>, SetOntologyAnnotationsAction, SetOntologyAnnotationsResult> {

    @Inject
    public SetOntologyAnnotationsActionHandler(ProjectManager projectManager,
                                               AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<SetOntologyAnnotationsAction> getActionClass() {
        return SetOntologyAnnotationsAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return asList(EDIT_ONTOLOGY, EDIT_ONTOLOGY_ANNOTATIONS);
    }

    @Override
    protected ChangeListGenerator<Set<OWLAnnotation>> getChangeListGenerator(SetOntologyAnnotationsAction action, Project project, ExecutionContext executionContext) {
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
    protected ChangeDescriptionGenerator<Set<OWLAnnotation>> getChangeDescription(SetOntologyAnnotationsAction action, Project project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLAnnotation>>("Edited ontology annotations");
    }

    @Override
    protected SetOntologyAnnotationsResult createActionResult(ChangeApplicationResult<Set<OWLAnnotation>> changeApplicationResult, SetOntologyAnnotationsAction action, Project project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new SetOntologyAnnotationsResult(project.getRootOntology().getAnnotations(), eventList);
    }
}

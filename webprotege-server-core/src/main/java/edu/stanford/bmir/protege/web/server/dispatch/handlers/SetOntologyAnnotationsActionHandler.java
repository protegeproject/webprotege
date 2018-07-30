package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.SetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.SetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY_ANNOTATIONS;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 01/08/2013
 */
public class SetOntologyAnnotationsActionHandler extends AbstractProjectChangeHandler<Set<OWLAnnotation>, SetOntologyAnnotationsAction, SetOntologyAnnotationsResult> {

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Inject
    public SetOntologyAnnotationsActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                               @Nonnull HasApplyChanges applyChanges,
                                               @Nonnull @RootOntology OWLOntology rootOntology) {
        super(accessManager, eventManager, applyChanges);
        this.rootOntology = rootOntology;
    }

    @Nonnull
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
    protected ChangeListGenerator<Set<OWLAnnotation>> getChangeListGenerator(SetOntologyAnnotationsAction action,
                                                                             ExecutionContext executionContext) {
        final Set<PropertyAnnotationValue> fromAnnotations = action.getFromAnnotations();
        final Set<PropertyAnnotationValue> toAnnotations = action.getToAnnotations();

        List<OWLOntologyChange> changeList = new ArrayList<>();

        OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager().getOWLDataFactory();
        for (PropertyAnnotationValue annotation : fromAnnotations) {
            if (!toAnnotations.contains(annotation)) {
                annotation.getValue().asAnnotationValue().ifPresent(av -> {
                    changeList.add(new RemoveOntologyAnnotation(rootOntology,
                            dataFactory.getOWLAnnotation(
                                    annotation.getProperty().getEntity().asOWLAnnotationProperty(),
                                    av
                            )));
                });
            }
        }
        for (PropertyAnnotationValue annotation : toAnnotations) {
            if (!fromAnnotations.contains(annotation)) {
                annotation.getValue().asAnnotationValue().ifPresent(av -> {
                    changeList.add(new AddOntologyAnnotation(rootOntology,
                            dataFactory.getOWLAnnotation(
                                    annotation.getProperty().getEntity().asOWLAnnotationProperty(),
                                    av
                            )));
                });
            }
        }
        return new FixedChangeListGenerator<>(changeList, Collections.emptySet(), "Edited ontology annotations");
    }

    @Override
    protected SetOntologyAnnotationsResult createActionResult(ChangeApplicationResult<Set<OWLAnnotation>> changeApplicationResult,
                                                              SetOntologyAnnotationsAction action,
                                                              ExecutionContext executionContext,
                                                              EventList<ProjectEvent<?>> eventList) {
        return new SetOntologyAnnotationsResult(rootOntology.getAnnotations(), eventList);
    }
}

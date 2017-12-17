package edu.stanford.bmir.protege.web.server.hierarchy;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeAction;
import edu.stanford.bmir.protege.web.shared.hierarchy.MoveHierarchyNodeResult;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.Collections;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 8 Dec 2017
 */
public class MoveHierarchyNodeActionHandler extends AbstractProjectChangeHandler<Boolean, MoveHierarchyNodeAction, MoveHierarchyNodeResult> {


    private final EventManager<ProjectEvent<?>> eventManager;

    private final OWLOntology rootOntology;

    private final OWLDataFactory dataFactory;

    @Inject
    public MoveHierarchyNodeActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                          @Nonnull HasApplyChanges applyChanges,
                                          OWLOntology rootOntology,
                                          OWLDataFactory dataFactory) {
        super(accessManager, eventManager, applyChanges);
        this.eventManager = eventManager;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    public Class<MoveHierarchyNodeAction> getActionClass() {
        return MoveHierarchyNodeAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(MoveHierarchyNodeAction action, ExecutionContext executionContext) {
        return new MoveEntityChangeListGenerator(rootOntology, dataFactory, action, eventManager);
    }

    @Override
    protected ChangeDescriptionGenerator<Boolean> getChangeDescription(MoveHierarchyNodeAction action, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>("Moved entity");
    }

    @Override
    protected MoveHierarchyNodeResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, MoveHierarchyNodeAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new MoveHierarchyNodeResult(changeApplicationResult.getSubject(),
                                           eventList);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_ONTOLOGY;
    }
}

package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateClassesChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_CLASS;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.Arrays.asList;

/**
 * Author: Matthew Horridge<br> Stanford University<br> Bio-Medical Informatics Research Group<br> Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractProjectChangeHandler<Set<OWLClass>, CreateClassesAction, CreateClassesResult> {

    @Nonnull
    private final CreateClassesChangeGeneratorFactory changeGeneratorFactory;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Inject
    public CreateClassesActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                      @Nonnull HasApplyChanges applyChanges,
                                      @Nonnull CreateClassesChangeGeneratorFactory changeFactory,
                                      @Nonnull EntityNodeRenderer entityNodeRenderer) {
        super(accessManager, eventManager, applyChanges);
        this.changeGeneratorFactory = checkNotNull(changeFactory);
        this.entityNodeRenderer = checkNotNull(entityNodeRenderer);
    }

    @Nonnull
    @Override
    public Class<CreateClassesAction> getActionClass() {
        return CreateClassesAction.class;
    }

    @Nonnull
    @Override
    protected List<BuiltInAction> getRequiredExecutableBuiltInActions(CreateClassesAction action) {
        return asList(CREATE_CLASS, EDIT_ONTOLOGY);
    }


    @Override
    protected ChangeListGenerator<Set<OWLClass>> getChangeListGenerator(CreateClassesAction action, ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.getSourceText(),
                                             action.getLangTag(),
                                             action.getParents());
    }

    @Override
    protected CreateClassesResult createActionResult(ChangeApplicationResult<Set<OWLClass>> changeApplicationResult, CreateClassesAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Set<OWLClass> classes = changeApplicationResult.getSubject();
        return new CreateClassesResult(action.getProjectId(),
                                       classes.stream().map(entityNodeRenderer::render).collect(toImmutableSet()),
                                       eventList);
    }
}

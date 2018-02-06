package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateObjectPropertiesChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateObjectPropertiesAction;
import edu.stanford.bmir.protege.web.shared.dispatch.actions.CreateObjectPropertiesResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_PROPERTY;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/03/2013
 */
public class CreateObjectPropertiesActionHandler extends AbstractProjectChangeHandler<Set<OWLObjectProperty>, CreateObjectPropertiesAction, CreateObjectPropertiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final CreateObjectPropertiesChangeGeneratorFactory changeGeneratorFactory;

    @Inject
    public CreateObjectPropertiesActionHandler(@Nonnull AccessManager accessManager,
                                               @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                               @Nonnull HasApplyChanges applyChanges,
                                               @Nonnull ProjectId projectId,
                                               @Nonnull CreateObjectPropertiesChangeGeneratorFactory changeGeneratorFactory) {
        super(accessManager, eventManager, applyChanges);
        this.projectId = projectId;
        this.changeGeneratorFactory = changeGeneratorFactory;
    }

    @Nonnull
    @Override
    public Class<CreateObjectPropertiesAction> getActionClass() {
        return CreateObjectPropertiesAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(CREATE_PROPERTY, EDIT_ONTOLOGY);
    }

    @Override
    protected ChangeListGenerator<Set<OWLObjectProperty>> getChangeListGenerator(CreateObjectPropertiesAction action,
                                                                                 ExecutionContext executionContext) {
        return changeGeneratorFactory.create(action.getSourceText(),
                                             action.getParent());
    }

    @Override
    protected CreateObjectPropertiesResult createActionResult(ChangeApplicationResult<Set<OWLObjectProperty>> changeApplicationResult, CreateObjectPropertiesAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        Set<OWLObjectProperty> result = changeApplicationResult.getSubject();
        return new CreateObjectPropertiesResult(projectId, ImmutableSet.copyOf(result), eventList);
    }
}

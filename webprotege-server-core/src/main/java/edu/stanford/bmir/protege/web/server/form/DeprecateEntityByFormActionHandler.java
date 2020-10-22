package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.form.DeprecateEntityByFormAction;
import edu.stanford.bmir.protege.web.shared.form.DeprecateEntityByFormResult;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-22
 */
public class DeprecateEntityByFormActionHandler extends AbstractProjectChangeHandler<OWLEntity, DeprecateEntityByFormAction, DeprecateEntityByFormResult> {

    @Nonnull
    private final DeprecateEntityByFormChangeListGeneratorFactory changeListGeneratorFactory;

    @Inject
    public DeprecateEntityByFormActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                              @Nonnull HasApplyChanges applyChanges,
                                              @Nonnull DeprecateEntityByFormChangeListGeneratorFactory changeListGeneratorFactory) {
        super(accessManager, eventManager, applyChanges);
        this.changeListGeneratorFactory = changeListGeneratorFactory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(DeprecateEntityByFormAction action) {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Nonnull
    @Override
    public Class<DeprecateEntityByFormAction> getActionClass() {
        return DeprecateEntityByFormAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntity> getChangeListGenerator(DeprecateEntityByFormAction action,
                                                                  ExecutionContext executionContext) {

        var entityToBeDeprecated = action.getEntity();
        return changeListGeneratorFactory.create(entityToBeDeprecated,
                                                 action.getDeprecationFormData(),
                                                 action.getReplacementEntity());
    }

    @Override
    protected DeprecateEntityByFormResult createActionResult(ChangeApplicationResult<OWLEntity> changeApplicationResult,
                                                             DeprecateEntityByFormAction action,
                                                             ExecutionContext executionContext,
                                                             EventList<ProjectEvent<?>> eventList) {
        return DeprecateEntityByFormResult.get(eventList);
    }
}

package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormsDataAction;
import edu.stanford.bmir.protege.web.shared.form.SetEntityFormDataResult;
import edu.stanford.bmir.protege.web.shared.form.data.FormData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class SetEntityFormsDataActionHandler extends AbstractProjectChangeHandler<OWLEntityData, SetEntityFormsDataAction, SetEntityFormDataResult> {

    @Nonnull
    private final EntityFormChangeListGeneratorFactory changeListGeneratorFactory;

    @Nonnull
    private final RenderingManager renderingManager;


    @Inject
    public SetEntityFormsDataActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                           @Nonnull HasApplyChanges applyChanges,
                                           @Nonnull EntityFormChangeListGeneratorFactory changeListGeneratorFactory,
                                           @Nonnull RenderingManager renderingManager) {
        super(accessManager, eventManager, applyChanges);
        this.changeListGeneratorFactory = checkNotNull(changeListGeneratorFactory);
        this.renderingManager = checkNotNull(renderingManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected SetEntityFormDataResult createActionResult(ChangeApplicationResult<OWLEntityData> changeApplicationResult,
                                                         SetEntityFormsDataAction action,
                                                         ExecutionContext executionContext,
                                                         EventList<ProjectEvent<?>> eventList) {
        return new SetEntityFormDataResult(eventList);
    }

    @Nonnull
    @Override
    public Class<SetEntityFormsDataAction> getActionClass() {
        return SetEntityFormsDataAction.class;
    }

    @Override
    protected ChangeListGenerator<OWLEntityData> getChangeListGenerator(SetEntityFormsDataAction action,
                                                                        ExecutionContext executionContext) {


        var pristineFormsData = action.getPristineFormsData();
        var editedFormsData = action.getEditedFormsData();
        var subject = action.getEntity();
        return changeListGeneratorFactory.create(subject,
                                                 pristineFormsData,
                                                 editedFormsData);
    }
}

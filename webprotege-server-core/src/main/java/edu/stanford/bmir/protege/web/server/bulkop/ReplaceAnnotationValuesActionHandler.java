package edu.stanford.bmir.protege.web.server.bulkop;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.bulkop.ReplaceAnnotationValuesAction;
import edu.stanford.bmir.protege.web.shared.bulkop.ReplaceAnnotationValuesResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */

public class ReplaceAnnotationValuesActionHandler extends AbstractProjectChangeHandler<Boolean, ReplaceAnnotationValuesAction, ReplaceAnnotationValuesResult> {

    @Nonnull
    private final ReplaceAnnotationValuesChangeListGeneratorFactory factory;

    @Inject
    public ReplaceAnnotationValuesActionHandler(@Nonnull AccessManager accessManager, @Nonnull EventManager<ProjectEvent<?>> eventManager, @Nonnull HasApplyChanges applyChanges, @Nonnull ReplaceAnnotationValuesChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<ReplaceAnnotationValuesAction> getActionClass() {
        return ReplaceAnnotationValuesAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(ReplaceAnnotationValuesAction action, ExecutionContext executionContext) {
        return factory.create(action.getEntities(),
                              action.getProperty(),
                              action.getMatchString(),
                              action.isRegex(),
                              action.getReplacement());
    }

    @Override
    protected ReplaceAnnotationValuesResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, ReplaceAnnotationValuesAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new ReplaceAnnotationValuesResult();
    }
}

package edu.stanford.bmir.protege.web.server.bulkop;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsAction;
import edu.stanford.bmir.protege.web.shared.bulkop.EditAnnotationsResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */

public class EditAnnotationValuesActionHandler extends AbstractProjectChangeHandler<Boolean, EditAnnotationsAction, EditAnnotationsResult> {

    @Nonnull
    private final EditAnnotationsChangeListGeneratorFactory factory;

    @Inject
    public EditAnnotationValuesActionHandler(@Nonnull AccessManager accessManager,
                                             @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                             @Nonnull HasApplyChanges applyChanges,
                                             @Nonnull EditAnnotationsChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<EditAnnotationsAction> getActionClass() {
        return EditAnnotationsAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(EditAnnotationsAction action, ExecutionContext executionContext) {
        return factory.create(action.getEntities(),
                              action.getOperation(),
                              action.getProperty(),
                              action.getLexicalValueExpression(),
                              action.isLexicalValueExpressionIsRegEx(),
                              action.getLangTagExpression(),
                              action.getNewAnnotationData(),
                              action.getCommitMessage());
    }

    @Override
    protected EditAnnotationsResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, EditAnnotationsAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return EditAnnotationsResult.get(eventList);
    }
}

package edu.stanford.bmir.protege.web.server.bulkop;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.bulkop.SetAnnotationValueAction;
import edu.stanford.bmir.protege.web.shared.bulkop.SetAnnotationValueResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Sep 2018
 */
public class SetAnnotationValueActionHandler extends AbstractProjectChangeHandler<Set<OWLEntity>, SetAnnotationValueAction, SetAnnotationValueResult> {

    @Nonnull
    private final SetAnnotationValueActionChangeListGeneratorFactory factory;

    @Inject
    public SetAnnotationValueActionHandler(@Nonnull AccessManager accessManager,
                                           @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                           @Nonnull HasApplyChanges applyChanges,
                                           @Nonnull SetAnnotationValueActionChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = checkNotNull(factory);
    }

    @Nonnull
    @Override
    public Class<SetAnnotationValueAction> getActionClass() {
        return SetAnnotationValueAction.class;
    }

    @Override
    protected ChangeListGenerator<Set<OWLEntity>> getChangeListGenerator(SetAnnotationValueAction action, ExecutionContext executionContext) {
        return factory.create(action.getEntities(),
                              action.getProperty(),
                              action.getValue(),
                              action.getCommitMessage());
    }

    @Override
    protected SetAnnotationValueResult createActionResult(ChangeApplicationResult<Set<OWLEntity>> changeApplicationResult, SetAnnotationValueAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return SetAnnotationValueResult.get(eventList);
    }
}

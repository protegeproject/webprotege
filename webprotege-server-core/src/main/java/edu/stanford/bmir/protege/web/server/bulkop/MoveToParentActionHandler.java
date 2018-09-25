package edu.stanford.bmir.protege.web.server.bulkop;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.shared.bulkop.MoveToParentAction;
import edu.stanford.bmir.protege.web.shared.bulkop.MoveToParentResult;
import edu.stanford.bmir.protege.web.shared.event.EventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Sep 2018
 */
public class MoveToParentActionHandler extends AbstractProjectChangeHandler<Boolean, MoveToParentAction, MoveToParentResult> {

    @Nonnull
    private MoveClassesChangeListGeneratorFactory factory;

    @Inject
    public MoveToParentActionHandler(@Nonnull AccessManager accessManager, @Nonnull EventManager<ProjectEvent<?>> eventManager, @Nonnull HasApplyChanges applyChanges, @Nonnull MoveClassesChangeListGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.factory = factory;
    }

    @Nonnull
    @Override
    public Class<MoveToParentAction> getActionClass() {
        return MoveToParentAction.class;
    }

    @Override
    protected ChangeListGenerator<Boolean> getChangeListGenerator(MoveToParentAction action, ExecutionContext executionContext) {
        if(action.getEntity().isOWLClass()) {
            ImmutableSet<OWLClass> clses = action.getEntities().stream().map(OWLEntity::asOWLClass).collect(toImmutableSet());
            return factory.create(clses, action.getEntity().asOWLClass());
        }
        return null;
    }

    @Override
    protected MoveToParentResult createActionResult(ChangeApplicationResult<Boolean> changeApplicationResult, MoveToParentAction action, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        return new MoveToParentResult();
    }
}

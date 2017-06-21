package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Joiner;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.crud.DeleteEntitiesChangeListGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 May 2017
 */
public class DeleteEntitiesActionHandler extends AbstractProjectChangeHandler<Set<OWLEntity>, DeleteEntitiesAction, DeleteEntitiesResult> {

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public DeleteEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                       @Nonnull HasApplyChanges applyChanges,
                                       @Nonnull RenderingManager renderer,
                                       @Nonnull OWLOntology rootOntology) {
        super(accessManager, eventManager, applyChanges);
        this.renderer = renderer;
        this.rootOntology = rootOntology;
    }

    @Override
    public Class<DeleteEntitiesAction> getActionClass() {
        return DeleteEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Set<OWLEntity>> getChangeListGenerator(DeleteEntitiesAction action,
                                                                         ExecutionContext executionContext) {

        return new DeleteEntitiesChangeListGenerator(action.getEntities(),
                                                     rootOntology);
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLEntity>> getChangeDescription(DeleteEntitiesAction action,
                                                                              ExecutionContext executionContext) {
        Set<OWLEntity> entities = action.getEntities();
        Joiner joiner = Joiner.on(", ").skipNulls();
        Object[] renderings = entities.stream()
                                      .map(this::getBrowserText)
                                      .sorted()
                                      .toArray();
        String deletedEntitiesRendering = joiner.join(renderings);
        return new FixedMessageChangeDescriptionGenerator<>(String.format("Deleted %s", deletedEntitiesRendering));
    }

    private String getBrowserText(OWLEntity e) {
        return renderer.getRendering(e).getBrowserText();
    }

    @Override
    protected DeleteEntitiesResult createActionResult(ChangeApplicationResult<Set<OWLEntity>> changeApplicationResult,
                                                      DeleteEntitiesAction action,
                                                      ExecutionContext executionContext,
                                                      EventList<ProjectEvent<?>> eventList) {
        return new DeleteEntitiesResult(eventList,
                                        changeApplicationResult.getSubject().orElse(Collections.emptySet()));
    }
}

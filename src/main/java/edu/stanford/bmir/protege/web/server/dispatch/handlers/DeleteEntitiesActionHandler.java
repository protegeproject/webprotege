package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Joiner;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.crud.DeleteEntitiesChangeListGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.DeleteEntitiesResult;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLEntity;

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

    @Inject
    public DeleteEntitiesActionHandler(ProjectManager projectManager,
                                       AccessManager accessManager) {
        super(projectManager, accessManager);
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
                                                                         Project project,
                                                                         ExecutionContext executionContext) {

        return new DeleteEntitiesChangeListGenerator(action.getEntities());
    }

    @Override
    protected ChangeDescriptionGenerator<Set<OWLEntity>> getChangeDescription(DeleteEntitiesAction action,
                                                                              Project project,
                                                                              ExecutionContext executionContext) {
        Set<OWLEntity> entities = action.getEntities();
        Joiner joiner = Joiner.on(", ").skipNulls();
        Object[] renderings = entities.stream()
                                      .map(e -> getBrowserText(project, e))
                                      .sorted()
                                      .toArray();
        String deletedEntitiesRendering = joiner.join(renderings);
        return new FixedMessageChangeDescriptionGenerator<>(String.format("Deleted %s", deletedEntitiesRendering));
    }

    private static String getBrowserText(Project project, OWLEntity e) {
        return project.getRenderingManager().getRendering(e).getBrowserText();
    }

    @Override
    protected DeleteEntitiesResult createActionResult(ChangeApplicationResult<Set<OWLEntity>> changeApplicationResult,
                                                      DeleteEntitiesAction action,
                                                      Project project,
                                                      ExecutionContext executionContext,
                                                      EventList<ProjectEvent<?>> eventList) {
        return new DeleteEntitiesResult(eventList,
                                        changeApplicationResult.getSubject().orElse(Collections.emptySet()));
    }
}

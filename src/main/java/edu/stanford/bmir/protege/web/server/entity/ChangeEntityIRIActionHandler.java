package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.ChangeEntityIRIAction;
import edu.stanford.bmir.protege.web.shared.entity.ChangeEntityIRIResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2017
 */
public class ChangeEntityIRIActionHandler extends AbstractHasProjectActionHandler<ChangeEntityIRIAction, ChangeEntityIRIResult> {


    private final EntityDiscussionThreadRepository discussionThreadRepository;

    @Inject
    public ChangeEntityIRIActionHandler(@Nonnull ProjectManager projectManager,
                                        @Nonnull AccessManager accessManager,
                                        @Nonnull EntityDiscussionThreadRepository discussionThreadRepository) {
        super(projectManager, accessManager);
        this.discussionThreadRepository = checkNotNull(discussionThreadRepository);
    }

    @Override
    public Class<ChangeEntityIRIAction> getActionClass() {
        return ChangeEntityIRIAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeEntityIRIResult execute(ChangeEntityIRIAction action,
                                            Project project,
                                            ExecutionContext executionContext) {
        OWLEntityRenamer renamer = new OWLEntityRenamer(project.getRootOntology().getOWLOntologyManager(),
                                                        project.getRootOntology().getImportsClosure());
        List<OWLOntologyChange> changeList = renamer.changeIRI(action.getEntity(), action.getTheNewIri());
        OWLEntityData oldRendering = project.getRenderingManager().getRendering(action.getEntity());
        ProjectId projectId = action.getProjectId();
        project.applyChanges(executionContext.getUserId(),
                             new FixedChangeListGenerator<>(changeList),
                             new FixedMessageChangeDescriptionGenerator<>(
                                     String.format("Changed %s IRI from %s to %s",
                                                   action.getEntity().getEntityType().getPrintName(),
                                                   action.getEntity().getIRI(),
                                                   action.getTheNewIri())
                             ));
        OWLEntity theNewEntity = project.getDataFactory().getOWLEntity(action.getEntity().getEntityType(),
                                                                       action.getTheNewIri());

        discussionThreadRepository.replaceEntity(projectId, action.getEntity(), theNewEntity);
        OWLEntityData newRendering = project.getRenderingManager().getRendering(theNewEntity);
        return new ChangeEntityIRIResult(project.getProjectId(),
                                         oldRendering,
                                         newRendering);
    }
}

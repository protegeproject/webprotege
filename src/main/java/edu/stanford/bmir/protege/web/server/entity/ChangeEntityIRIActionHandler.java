package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.issues.EntityDiscussionThreadRepository;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.ChangeEntityIRIAction;
import edu.stanford.bmir.protege.web.shared.entity.ChangeEntityIRIResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2017
 */
public class ChangeEntityIRIActionHandler extends AbstractHasProjectActionHandler<ChangeEntityIRIAction, ChangeEntityIRIResult> {


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final HasApplyChanges applyChanges;

    @Nonnull
    private final EntityDiscussionThreadRepository discussionThreadRepository;


    @Inject
    public ChangeEntityIRIActionHandler(@Nonnull AccessManager accessManager,
                                        @Nonnull ProjectId projectId,
                                        @Nonnull @RootOntology OWLOntology rootOntology,
                                        @Nonnull RenderingManager renderer,
                                        @Nonnull OWLDataFactory dataFactory,
                                        @Nonnull HasApplyChanges applyChanges,
                                        @Nonnull EntityDiscussionThreadRepository discussionThreadRepository) {
        super(accessManager);
        this.projectId = projectId;
        this.rootOntology = rootOntology;
        this.renderer = renderer;
        this.dataFactory = dataFactory;
        this.applyChanges = applyChanges;
        this.discussionThreadRepository = discussionThreadRepository;
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
    public ChangeEntityIRIResult execute(ChangeEntityIRIAction action,
                                            ExecutionContext executionContext) {
        OWLEntityRenamer renamer = new OWLEntityRenamer(rootOntology.getOWLOntologyManager(),
                                                        rootOntology.getImportsClosure());
        List<OWLOntologyChange> changeList = renamer.changeIRI(action.getEntity(), action.getTheNewIri());
        OWLEntityData oldRendering = renderer.getRendering(action.getEntity());
        applyChanges.applyChanges(executionContext.getUserId(),
                             new FixedChangeListGenerator<>(changeList),
                             new FixedMessageChangeDescriptionGenerator<>(
                                     String.format("Changed %s IRI from %s to %s",
                                                   action.getEntity().getEntityType().getPrintName(),
                                                   action.getEntity().getIRI(),
                                                   action.getTheNewIri())
                             ));
        OWLEntity theNewEntity = dataFactory.getOWLEntity(action.getEntity().getEntityType(),
                                                          action.getTheNewIri());

        discussionThreadRepository.replaceEntity(projectId, action.getEntity(), theNewEntity);
        OWLEntityData newRendering = renderer.getRendering(theNewEntity);
        return new ChangeEntityIRIResult(projectId,
                                         oldRendering,
                                         newRendering);
    }
}

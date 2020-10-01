package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.index.IndividualsIndex;
import edu.stanford.bmir.protege.web.server.index.IndividualsQueryResult;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsActionHandler extends AbstractProjectActionHandler<GetIndividualsAction, GetIndividualsResult> {

    private static final Logger logger = LoggerFactory.getLogger(GetIndividualsActionHandler.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final IndividualsIndex individualsIndex;

    @Inject
    public GetIndividualsActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull EntityNodeRenderer entityNodeRenderer,
                                       @Nonnull IndividualsIndex individualsIndex) {
        super(accessManager);
        this.projectId = projectId;
        this.renderingManager = renderingManager;
        this.entityNodeRenderer = entityNodeRenderer;
        this.individualsIndex = individualsIndex;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(GetIndividualsAction action) {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetIndividualsResult execute(@Nonnull GetIndividualsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        OWLClass type = action.getType().orElse(DataFactory.getOWLThing());
        IndividualsQueryResult result;
        String filterString = action.getFilterString();
        PageRequest pageRequest = action.getPageRequest().orElse(PageRequest.requestSinglePage());
        result = individualsIndex.getIndividuals(type,
                                                 action.getInstanceRetrievalMode(),
                                                 filterString,
                                                 pageRequest);
        OWLClassData typeData = renderingManager.getClassData(type);
        logger.info(BROWSING,
                    "{} {} retrieved instances of {}",
                    projectId,
                    executionContext.getUserId(),
                    type);
        Page<OWLNamedIndividual> pg = result.getIndividuals();
        Page<EntityNode> entityNodes = pg.transform(entityNodeRenderer::render);
        Optional<OWLClassData> renderedType = action.getType().map(t -> typeData);
        return new GetIndividualsResult(renderedType,
                                        entityNodes,
                                        result.getIndividualsCount());
    }

    @Nonnull
    @Override
    public Class<GetIndividualsAction> getActionClass() {
        return GetIndividualsAction.class;
    }
}

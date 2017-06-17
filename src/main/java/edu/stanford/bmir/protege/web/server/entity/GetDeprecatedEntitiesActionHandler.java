package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.server.pagination.PageCollector.toPageNumber;
import static edu.stanford.bmir.protege.web.server.util.ProtegeStreams.entityStream;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
public class GetDeprecatedEntitiesActionHandler extends AbstractHasProjectActionHandler<GetDeprecatedEntitiesAction, GetDeprecatedEntitiesResult> {

    @Inject
    public GetDeprecatedEntitiesActionHandler(@Nonnull ProjectManager projectManager,
                                              AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetDeprecatedEntitiesAction> getActionClass() {
        return GetDeprecatedEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetDeprecatedEntitiesResult execute(GetDeprecatedEntitiesAction action,
                                                  Project project,
                                                  ExecutionContext executionContext) {
        PageRequest pageRequest = action.getPageRequest();
        Optional<Page<OWLEntityData>> page = entityStream(action.getEntityTypes(),
                                                          project.getRootOntology(),
                                                          Imports.INCLUDED)
                .filter(project::isDeprecated)
                .map(e -> project.getRenderingManager().getRendering(e))
                .sorted()
                .collect(toPageNumber(pageRequest.getPageNumber())
                                 .forPageSize(pageRequest.getPageSize()));
        return new GetDeprecatedEntitiesResult(page.orElse(Page.emptyPage()));
    }


}

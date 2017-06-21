package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.render.DeprecatedEntityChecker;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesResult;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLOntology;
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

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final DeprecatedEntityChecker deprecatedEntityChecker;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetDeprecatedEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull OWLOntology rootOntology,
                                              @Nonnull DeprecatedEntityChecker deprecatedEntityChecker,
                                              @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.rootOntology = rootOntology;
        this.deprecatedEntityChecker = deprecatedEntityChecker;
        this.renderingManager = renderingManager;
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
    public GetDeprecatedEntitiesResult execute(GetDeprecatedEntitiesAction action,
                                                  ExecutionContext executionContext) {
        PageRequest pageRequest = action.getPageRequest();
        Optional<Page<OWLEntityData>> page = entityStream(action.getEntityTypes(),
                                                          rootOntology,
                                                          Imports.INCLUDED)
                .filter(deprecatedEntityChecker::isDeprecated)
                .map(renderingManager::getRendering)
                .sorted()
                .collect(toPageNumber(pageRequest.getPageNumber())
                                 .forPageSize(pageRequest.getPageSize()));
        return new GetDeprecatedEntitiesResult(page.orElse(Page.emptyPage()));
    }


}

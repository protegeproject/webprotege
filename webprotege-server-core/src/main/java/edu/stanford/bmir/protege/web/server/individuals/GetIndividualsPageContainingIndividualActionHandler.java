package edu.stanford.bmir.protege.web.server.individuals;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.index.IndividualsIndex;
import edu.stanford.bmir.protege.web.server.index.IndividualsQueryResult;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualAction;
import edu.stanford.bmir.protege.web.shared.individuals.GetIndividualsPageContainingIndividualResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
public class GetIndividualsPageContainingIndividualActionHandler extends AbstractProjectActionHandler<GetIndividualsPageContainingIndividualAction, GetIndividualsPageContainingIndividualResult> {

    @Nonnull
    private final IndividualsIndex individualsIndex;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Inject
    public GetIndividualsPageContainingIndividualActionHandler(@Nonnull AccessManager accessManager, @Nonnull IndividualsIndex individualsIndex, @Nonnull EntityNodeRenderer renderer) {
        super(accessManager);
        this.individualsIndex = checkNotNull(individualsIndex);
        this.renderer = checkNotNull(renderer);
    }

    @Nonnull
    @Override
    public Class<GetIndividualsPageContainingIndividualAction> getActionClass() {
        return GetIndividualsPageContainingIndividualAction.class;
    }

    @Nonnull
    @Override
    public GetIndividualsPageContainingIndividualResult execute(@Nonnull GetIndividualsPageContainingIndividualAction action, @Nonnull ExecutionContext executionContext) {
        IndividualsQueryResult result = individualsIndex.getIndividualsPageContaining(action.getIndividual(),
                                                                                      action.getPreferredType(),
                                                                                      action.getPreferredMode(),
                                                                                      200);
        Page<EntityNode> entityNodesPage = result.getIndividuals().transform(renderer::render);
        ImmutableSet<EntityNode> types =
                individualsIndex
                        .getTypes(action.getIndividual())
                        .map(renderer::render)
                        .collect(toImmutableSet());
        return GetIndividualsPageContainingIndividualResult.get(action.getIndividual(),
                                                                entityNodesPage,
                                                                renderer.render(result.getType()),
                                                                result.getMode(),
                                                                types);
    }
}

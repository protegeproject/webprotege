package edu.stanford.bmir.protege.web.server.match;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.trigger.TriggerRunner;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class GetMatchingEntitiesActionHandler extends AbstractProjectActionHandler<GetMatchingEntitiesAction, GetMatchingEntitiesResult>{

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final MatcherFactory matcherFactory;


    @Inject
    public GetMatchingEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                            @Nonnull OWLOntology rootOntology,
                                            @Nonnull RenderingManager renderingManager,
                                            @Nonnull MatcherFactory matcherFactory) {
        super(accessManager);
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.matcherFactory = matcherFactory;
    }

    @Nonnull
    @Override
    public Class<GetMatchingEntitiesAction> getActionClass() {
        return GetMatchingEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetMatchingEntitiesResult execute(@Nonnull GetMatchingEntitiesAction action, @Nonnull ExecutionContext executionContext) {
        Matcher<OWLEntity> matcher = matcherFactory.getMatcher((RootCriteria) action.getCriteria());
        ImmutableList<OWLEntityData> result = rootOntology.getSignature().stream()
                                                          .filter(matcher::matches)
                                                          .map(entity -> DataFactory.getOWLEntityData(entity, renderingManager.getShortForm(entity)))
                                                          .collect(toImmutableList());
        return GetMatchingEntitiesResult.get(result);
    }
}

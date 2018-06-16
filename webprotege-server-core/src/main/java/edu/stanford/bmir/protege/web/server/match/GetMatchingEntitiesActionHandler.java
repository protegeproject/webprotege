package edu.stanford.bmir.protege.web.server.match;

import com.google.common.base.Stopwatch;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.concurrent.TimeUnit;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
public class GetMatchingEntitiesActionHandler extends AbstractProjectActionHandler<GetMatchingEntitiesAction, GetMatchingEntitiesResult>{

    private static Logger logger = LoggerFactory.getLogger(GetMatchingEntitiesActionHandler.class);

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
        Stopwatch stopwatch = Stopwatch.createStarted();
        Matcher<OWLEntity> matcher = matcherFactory.getMatcher((RootCriteria) action.getCriteria());
        ImmutableList<OWLEntityData> result = rootOntology.getSignature().stream()
                                                          .filter(matcher::matches)
                                                          .map(entity -> DataFactory.getOWLEntityData(entity, renderingManager.getShortForm(entity)))
                                                          .collect(toImmutableList());
        stopwatch.stop();
        logger.info("Answer criteria query in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return GetMatchingEntitiesResult.get(result);
    }
}

package edu.stanford.bmir.protege.web.server.metrics;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsResult;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class GetMetricsActionHandler extends AbstractProjectActionHandler<GetMetricsAction, GetMetricsResult> {

    @Nonnull
    private final OWLAPIProjectMetricsManager metricsManager;

    @Inject
    public GetMetricsActionHandler(@Nonnull AccessManager accessManager,
                                   @Nonnull OWLAPIProjectMetricsManager metricsManager) {
        super(accessManager);
        this.metricsManager = metricsManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public GetMetricsResult execute(@Nonnull GetMetricsAction action, @Nonnull ExecutionContext executionContext) {
        List<MetricValue> metrics = metricsManager.getMetrics();
        return new GetMetricsResult(ImmutableList.copyOf(metrics));
    }

    @Nonnull
    @Override
    public Class<GetMetricsAction> getActionClass() {
        return GetMetricsAction.class;
    }
}

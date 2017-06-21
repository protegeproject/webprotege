package edu.stanford.bmir.protege.web.server.metrics;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
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
public class GetMetricsActionHandler extends AbstractHasProjectActionHandler<GetMetricsAction, GetMetricsResult> {

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

    @Override
    public GetMetricsResult execute(GetMetricsAction action, ExecutionContext executionContext) {
        List<MetricValue> metrics = metricsManager.getMetrics();
        return new GetMetricsResult(ImmutableList.copyOf(metrics));
    }

    @Override
    public Class<GetMetricsAction> getActionClass() {
        return GetMetricsAction.class;
    }
}

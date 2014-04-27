package edu.stanford.bmir.protege.web.server.owlapi.metrics;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsAction;
import edu.stanford.bmir.protege.web.shared.metrics.GetMetricsResult;
import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class GetMetricsActionHandler extends AbstractHasProjectActionHandler<GetMetricsAction, GetMetricsResult> {
    @Override
    protected RequestValidator<GetMetricsAction> getAdditionalRequestValidator(GetMetricsAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetMetricsResult execute(GetMetricsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        List<MetricValue> metrics = project.getMetricsManager().getMetrics();
        return new GetMetricsResult(ImmutableList.copyOf(metrics));
    }

    @Override
    public Class<GetMetricsAction> getActionClass() {
        return GetMetricsAction.class;
    }
}

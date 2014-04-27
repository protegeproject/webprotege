package edu.stanford.bmir.protege.web.shared.metrics;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class MetricsChangedEvent extends ProjectEvent<MetricsChangedHandler> {

    private static final transient Type<MetricsChangedHandler> TYPE = new Type<MetricsChangedHandler>();

    public MetricsChangedEvent(ProjectId source) {
        super(source);
    }

    private MetricsChangedEvent() {
    }

    public static Type<MetricsChangedHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<MetricsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MetricsChangedHandler handler) {
        handler.handleMetricsChanged(this);
    }
}

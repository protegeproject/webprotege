package edu.stanford.bmir.protege.web.shared.metrics;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/04/2014
 */
public class MetricsChangedEvent extends ProjectEvent<MetricsChangedHandler> {

    private static final transient Event.Type<MetricsChangedHandler> TYPE = new Event.Type<MetricsChangedHandler>();

    public MetricsChangedEvent(ProjectId source) {
        super(source);
    }

    private MetricsChangedEvent() {
    }

    public static Event.Type<MetricsChangedHandler> getType() {
        return TYPE;
    }

    @Override
    public Event.Type<MetricsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MetricsChangedHandler handler) {
        handler.handleMetricsChanged(this);
    }
}

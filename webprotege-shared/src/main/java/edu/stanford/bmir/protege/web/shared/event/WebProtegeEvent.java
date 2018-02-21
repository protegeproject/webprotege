package edu.stanford.bmir.protege.web.shared.event;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/03/2013
 */
public abstract class WebProtegeEvent<H> implements IsSerializable {

    private ProjectId projectId;

    protected WebProtegeEvent() {
    }

    public abstract Event.Type<H> getAssociatedType();

    protected abstract void dispatch(H handler);

    public ProjectId getSource() {
        return projectId;
    }

    protected void setSource(ProjectId source) {
        this.projectId = source;
    }

    public String toDebugString() {
        return "WebProtegeEvent";
    }

    public Event<H> asGWTEvent() {
        return new Event<H>() {

            @Override
            public Object getSource() {
                return WebProtegeEvent.this.getSource();
            }

            @Override
            public Type<H> getAssociatedType() {
                return WebProtegeEvent.this.getAssociatedType();
            }

            @Override
            protected void dispatch(H h) {
                WebProtegeEvent.this.dispatch(h);
            }
        };
    }

}

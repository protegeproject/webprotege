package edu.stanford.bmir.protege.web.shared.event;

import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/03/2013
 */
public class BrowserTextChangedEvent extends ProjectEvent<BrowserTextChangedHandler> {

    public transient static final Event.Type<BrowserTextChangedHandler> ON_BROWSER_TEXT_CHANGED = new Event.Type<>();


    private OWLEntity entity;

    private String newBrowserText;

    public BrowserTextChangedEvent(OWLEntity entity, String newBrowserText, ProjectId projectId) {
        super(projectId);
        this.entity = entity;
        this.newBrowserText = newBrowserText;
    }

    /**
     * For serialization purposes only
     */
    private BrowserTextChangedEvent() {
    }

    public OWLEntity getEntity() {
        return entity;
    }

    public String getNewBrowserText() {
        return newBrowserText;
    }

    @Override
    public Event.Type<BrowserTextChangedHandler> getAssociatedType() {
        return ON_BROWSER_TEXT_CHANGED;
    }

    @Override
    protected void dispatch(BrowserTextChangedHandler handler) {
        handler.browserTextChanged(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BrowserTextChangedEvent");
        sb.append("Entity(");
        sb.append(entity);
        sb.append(") NewBroswerText(");
        sb.append(newBrowserText);
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }
}

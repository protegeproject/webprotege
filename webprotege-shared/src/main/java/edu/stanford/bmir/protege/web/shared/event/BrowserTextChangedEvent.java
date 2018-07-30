package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.collect.ImmutableMap;
import com.google.web.bindery.event.shared.Event;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

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

    private ImmutableMap<DictionaryLanguage, String> shortForms;

    public BrowserTextChangedEvent(OWLEntity entity, String newBrowserText, ProjectId projectId, ImmutableMap<DictionaryLanguage, String> shortForms) {
        super(projectId);
        this.entity = entity;
        this.newBrowserText = newBrowserText;
        this.shortForms = shortForms;
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

    @Nonnull
    public ImmutableMap<DictionaryLanguage, String> getShortForms() {
        return shortForms;
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

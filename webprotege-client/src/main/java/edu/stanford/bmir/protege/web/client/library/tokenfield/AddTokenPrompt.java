package edu.stanford.bmir.protege.web.client.library.tokenfield;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public interface AddTokenPrompt<T> {

    void displayAddTokenPrompt(ClickEvent event, AddTokenCallback<T> callback);
}

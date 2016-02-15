package edu.stanford.bmir.protege.web.client.ui.tab;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
public interface ProjectTabWidget {

    void setMenuVisible(boolean visible);

    void addMenuItem(String itemName, ClickHandler clickHandler);


}

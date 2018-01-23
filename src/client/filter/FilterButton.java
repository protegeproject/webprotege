package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public interface FilterButton extends IsWidget, HasClickHandlers {

    void setFiltered(boolean filtered);

    boolean isFiltered();
}

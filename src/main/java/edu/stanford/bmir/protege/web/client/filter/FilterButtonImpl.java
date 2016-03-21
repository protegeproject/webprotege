package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterButtonImpl extends Composite implements FilterButton {

    interface FilterButtonImplUiBinder extends UiBinder<HTMLPanel, FilterButtonImpl> {

    }

    private static FilterButtonImplUiBinder ourUiBinder = GWT.create(FilterButtonImplUiBinder.class);

    public FilterButtonImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setFiltered(boolean filtered) {
        if(filtered) {
            addStyleName(getSelectedStyle());
        }
        else {
            removeStyleName(getSelectedStyle());
        }
    }

    private String getSelectedStyle() {
        return WebProtegeClientBundle.BUNDLE.buttons().toolbarButtonSelected();
    }

    @Override
    public boolean isFiltered() {
        return getStyleName().contains(getSelectedStyle());
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
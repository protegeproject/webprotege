package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public class FilterGroupHeader extends Composite {

    interface FilterGroupHeaderUiBinder extends UiBinder<HTMLPanel, FilterGroupHeader> {

    }

    private static FilterGroupHeaderUiBinder ourUiBinder = GWT.create(FilterGroupHeaderUiBinder.class);

    @UiField
    Label label;

    @UiField
    HasClickHandlers selectNoneButton;

    @UiField
    HasClickHandlers selectAllButton;

    public FilterGroupHeader() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


    public void setLabel(String label) {
        this.label.setText(label);
    }

    public void setSelectAllClickHandler(ClickHandler clickHandler) {
        selectAllButton.addClickHandler(clickHandler);
    }

    public void setSelectNoneClickHandler(ClickHandler clickHandler) {
        selectNoneButton.addClickHandler(clickHandler);
    }
}
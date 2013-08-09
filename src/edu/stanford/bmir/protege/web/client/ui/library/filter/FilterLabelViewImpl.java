package edu.stanford.bmir.protege.web.client.ui.library.filter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/07/2013
 */
public class FilterLabelViewImpl {

    interface FilterLabelViewImplUiBinder extends UiBinder<HTMLPanel, FilterLabelViewImpl> {

    }

    private static FilterLabelViewImplUiBinder ourUiBinder = GWT.create(FilterLabelViewImplUiBinder.class);

    public FilterLabelViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);

    }
}
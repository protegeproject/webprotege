package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Apr 2017
 */
public class SearchResultView extends Composite {

    interface SearchResultViewUiBinder extends UiBinder<HTMLPanel, SearchResultView> {

    }

    private static SearchResultViewUiBinder ourUiBinder = GWT.create(SearchResultViewUiBinder.class);

    @UiField
    Image iconField;

    @UiField
    HTML renderingField;

    public SearchResultView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void setIcon(DataResource icon) {
        iconField.setUrl(icon.getSafeUri());
    }

    public void setFieldRendering(SafeHtml fieldRendering) {
        renderingField.setHTML(fieldRendering);
    }
}
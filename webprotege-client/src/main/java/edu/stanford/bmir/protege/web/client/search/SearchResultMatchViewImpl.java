package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class SearchResultMatchViewImpl extends Composite implements SearchResultMatchView {

    interface SearchResultMatchViewImplUiBinder extends UiBinder<HTMLPanel, SearchResultMatchViewImpl> {
    }

    private static SearchResultMatchViewImplUiBinder ourUiBinder = GWT.create(SearchResultMatchViewImplUiBinder.class);

    @UiField
    HTML renderingField;

    @UiField
    Label languageField;

    @Inject
    public SearchResultMatchViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setValue(@Nonnull String value) {
        renderingField.setTitle(value);
    }

    @Override
    public void setRendering(@Nonnull SafeHtml rendering) {
        renderingField.setHTML(rendering);
    }

    @Override
    public void setLanguageRendering(@Nonnull String languageRendering) {
        languageField.setText(languageRendering);
    }
}
package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class LangTagMatchesCriteriaViewImpl extends Composite implements LangTagMatchesCriteriaView {

    interface LangTagMatchesCriteriaViewImplUiBinder extends UiBinder<HTMLPanel, LangTagMatchesCriteriaViewImpl> {

    }

    private static LangTagMatchesCriteriaViewImplUiBinder ourUiBinder = GWT.create(LangTagMatchesCriteriaViewImplUiBinder.class);

    @UiField
    TextBox patternField;

    @Inject
    public LangTagMatchesCriteriaViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public String getPattern() {
        return patternField.getValue();
    }

    @Override
    public void setPattern(@Nonnull String pattern) {
        patternField.setValue(pattern);
    }
}
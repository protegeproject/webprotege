package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageTagFormatter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Aug 2018
 */
public class LanguageUsageViewImpl extends Composite implements LanguageUsageView {

    interface LanguageUsageViewImplUiBinder extends UiBinder<HTMLPanel, LanguageUsageViewImpl> {

    }

    private static LanguageUsageViewImplUiBinder ourUiBinder = GWT.create(LanguageUsageViewImplUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl propertyField;

    @UiField
    Label langTagField;

    @UiField
    Label usageCountField;

    @Inject
    public LanguageUsageViewImpl(@Nonnull PrimitiveDataEditorImpl propertyField) {
        this.propertyField = propertyField;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setProperty(@Nonnull OWLAnnotationPropertyData property) {
        propertyField.setValue(property);
    }

    @Override
    public void setLanguageTag(@Nonnull String langTag) {
        langTagField.setText(LanguageTagFormatter.format(langTag));
    }

    @Override
    public void setUsageCount(int usageCount) {
        usageCountField.setText(Integer.toString(usageCount));
    }
}
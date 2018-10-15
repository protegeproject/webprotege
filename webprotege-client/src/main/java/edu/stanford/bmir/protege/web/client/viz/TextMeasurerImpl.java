package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public class TextMeasurerImpl extends Composite implements TextMeasurer {

    interface TextMeasurerImplUiBinder extends UiBinder<HTMLPanel, TextMeasurerImpl> {

    }

    private static TextMeasurerImplUiBinder ourUiBinder = GWT.create(TextMeasurerImplUiBinder.class);

    @UiField
    HTMLPanel measuringElement;

    private String styleNames = "";

    public TextMeasurerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setStyleNames(@Nonnull String styles) {
        if(!this.styleNames.isEmpty()) {
            measuringElement.removeStyleName(styles);
        }
        styleNames = styles;
        measuringElement.addStyleName(styles);
    }

    @Nonnull
    @Override
    public TextDimensions getTextDimensions(@Nonnull String text) {
        Element element = measuringElement.getElement();
        element.setInnerText(text);
        return TextDimensions.get(element.getClientWidth(), element.getClientHeight());
    }
}
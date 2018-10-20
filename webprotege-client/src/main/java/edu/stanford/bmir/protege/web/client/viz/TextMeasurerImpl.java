package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15 Oct 2018
 */
public class TextMeasurerImpl extends Composite implements TextMeasurer {

    private static final int DEFAULT_STROKE_WIDTH = 4;

    interface TextMeasurerImplUiBinder extends UiBinder<HTMLPanel, TextMeasurerImpl> {

    }

    private static TextMeasurerImplUiBinder ourUiBinder = GWT.create(TextMeasurerImplUiBinder.class);

    @UiField
    HTMLPanel measuringElement;

    private final String basicStyleName;

    public TextMeasurerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        basicStyleName = measuringElement.getStyleName();
    }

    @Override
    public void setStyleNames(@Nonnull String styles) {
        measuringElement.setStyleName(styles);
        measuringElement.addStyleName(basicStyleName);
    }

    @Nonnull
    @Override
    public TextDimensions getTextDimensions(@Nonnull String text) {
        Element element = measuringElement.getElement();
        element.setInnerText(text);
        int clientWidth = element.getClientWidth();
        int clientHeight = element.getClientHeight();
        if(clientWidth < 0) {
            clientWidth = 0;
        }
        if(clientHeight < 0) {
            clientHeight = 0;
        }
        return TextDimensions.get(clientWidth, clientHeight);
    }

    @Override
    public double getStrokeWidth() {
        elemental.dom.Element element = (elemental.dom.Element) measuringElement.getElement();
        CSSStyleDeclaration computedStyle = Browser.getWindow().getComputedStyle(element, null);
        String strokeWidth = computedStyle.getPropertyValue("stroke-width");
        if(strokeWidth.isEmpty()) {
            return DEFAULT_STROKE_WIDTH;
        }
        if(strokeWidth.endsWith("px")) {
            strokeWidth = strokeWidth.substring(strokeWidth.length() - 2);
        }
        try {
            return Double.parseDouble(strokeWidth);
        } catch (NumberFormatException e) {
            return DEFAULT_STROKE_WIDTH;
        }
    }
}
package edu.stanford.bmir.protege.web.client.library.text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;

import javax.annotation.Nullable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class PlaceholderTextBox extends TextBox implements HasPlaceholder {

    private static final String ATTRIBUTE_NAME = "placeholder";

    public static final String INVALID_VALUE_BORDER_COLOR = "red";

    @Nullable
    private RegExp regExp = null;

    public PlaceholderTextBox() {
        addValueChangeHandler(event -> doValidation());
    }

    public PlaceholderTextBox(Element element) {
        super(element);
        addValueChangeHandler(event -> doValidation());
    }

    @Override
    public String getPlaceholder() {
        return getElement().getAttribute(ATTRIBUTE_NAME);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        getElement().setAttribute("placeholder", placeholder);
    }

    public void setValidation(String regexp) {
        this.regExp = RegExp.compile(regexp);
    }

    private void doValidation() {
        if(regExp != null) {
            GWT.log("Performing validation");
            regExp.setLastIndex(0);
            if(regExp.test(getValue())) {
                GWT.log("Value is valid");
                removeErrorStyle();
            }
            else {
                GWT.log("Value is not valid");
                applyErrorStyle();
            }
        }
    }

    private void applyErrorStyle() {
        getElement().getStyle().setBorderColor(INVALID_VALUE_BORDER_COLOR);
    }

    private void removeErrorStyle() {
        getElement().getStyle().clearBorderColor();
    }
}

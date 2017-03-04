package edu.stanford.bmir.protege.web.client.library.text;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class PlaceholderTextBox extends TextBox implements HasPlaceholder {

    private static final String ATTRIBUTE_NAME = "placeholder";

    public PlaceholderTextBox() {
    }

    public PlaceholderTextBox(Element element) {
        super(element);
    }

    @Override
    public String getPlaceholder() {
        return getElement().getAttribute(ATTRIBUTE_NAME);
    }

    @Override
    public void setPlaceholder(String placeholder) {
        getElement().setAttribute("placeholder", placeholder);
    }
}

package edu.stanford.bmir.protege.web.client.ui.library.text;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextArea;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/04/2013
 */
public class PlaceholderTextArea extends TextArea implements HasPlaceholder {

    private static final String ATTRIBUTE_NAME = "placeholder";

    public PlaceholderTextArea() {
    }

    public PlaceholderTextArea(Element element) {
        super(element);
    }

    @Override
    public String getPlaceholder() {
        final String result = getElement().getAttribute(ATTRIBUTE_NAME);
        return result == null ? "" : result;
    }

    @Override
    public void setPlaceholder(String placeholder) {
        getElement().setAttribute(ATTRIBUTE_NAME, placeholder);
    }
}

package edu.stanford.bmir.protege.web.client.ui.library.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.InlineLabel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class WebProtegeFormLabel extends InlineLabel {

    public WebProtegeFormLabel() {
        setupStyle();
    }

    public WebProtegeFormLabel(String text) {
        super(text);
        setupStyle();
    }

    public WebProtegeFormLabel(String text, Direction dir) {
        super(text, dir);
        setupStyle();
    }

    public WebProtegeFormLabel(String text, DirectionEstimator directionEstimator) {
        super(text, directionEstimator);
        setupStyle();
    }

    public WebProtegeFormLabel(Element element) {
        super(element);
        setupStyle();
    }


    private void setupStyle() {
        addStyleName("web-protege-form-label");
    }
}

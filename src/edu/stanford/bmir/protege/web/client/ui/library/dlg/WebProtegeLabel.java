package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.user.client.ui.Label;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public class WebProtegeLabel extends Label {

    public static final String STYLE_NAME = "web-protege-label";

    public WebProtegeLabel() {
        setupStyle();
    }

    public WebProtegeLabel(String text) {
        super(text);
        setupStyle();
    }

    public WebProtegeLabel(String text, Direction dir) {
        super(text, dir);
        setupStyle();
    }

    public WebProtegeLabel(String text, DirectionEstimator directionEstimator) {
        super(text, directionEstimator);
        setupStyle();
    }

    public WebProtegeLabel(String text, boolean wordWrap) {
        super(text, wordWrap);
        setupStyle();
    }

    public WebProtegeLabel(Element element) {
        super(element);
        setupStyle();
    }

    private void setupStyle() {
        addStyleName(STYLE_NAME);
    }
}

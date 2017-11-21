package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.shared.DirectionEstimator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 * <p>
 *     Represents a label that is styled specifically for the purposes of acting as a label on
 *     a {@link WebProtegeDialog}.
 * </p>
 */
public class WebProtegeDialogLabel extends WebProtegeLabel {

    public static final String STYLE_NAME = "web-protege-settings-label";

    public WebProtegeDialogLabel() {
        setupStyle();
    }

    public WebProtegeDialogLabel(String text) {
        super(text);
        setupStyle();
    }

    public WebProtegeDialogLabel(String text, Direction dir) {
        super(text, dir);
        setupStyle();
    }

    public WebProtegeDialogLabel(String text, DirectionEstimator directionEstimator) {
        super(text, directionEstimator);
        setupStyle();
    }

    public WebProtegeDialogLabel(String text, boolean wordWrap) {
        super(text, wordWrap);
        setupStyle();
    }

    public WebProtegeDialogLabel(Element element) {
        super(element);
        setupStyle();
    }

    private void setupStyle() {
        addStyleName(STYLE_NAME);
    }
}

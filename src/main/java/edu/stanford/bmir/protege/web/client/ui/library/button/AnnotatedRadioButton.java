package edu.stanford.bmir.protege.web.client.ui.library.button;

import com.google.gwt.user.client.ui.RadioButton;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 * <P>
 *     A {@link RadioButton} whose label consists of a main label and a piece of descriptive text.  The descriptive
 *     text is placed below the main label.
 * </P>
 */
public class AnnotatedRadioButton extends RadioButton {

    public static final String STYLE_WEB_PROTEGE_ANNOTATED_RADIO_BUTTON_LABEL = "web-protege-annotated-radio-button-label";

    /**
     * The style of the main label
     */
    public static final String STYLE_WEB_PROTEGE_ANNOTATED_RADIO_BUTTON_MAIN_LABEL = "web-protege-annotated-radio-button-main-label";


    public static final String STYLE_WEB_PROTEGE_ANNOTATED_RADIO_BUTTON_DESCRIPTION_LABEL = "web-protege-annotated-radio-button-description-label";

    public AnnotatedRadioButton(String name, String label, String description) {
        super(name, createLabel(label, description), true);
    }

    private static String createLabel(String label, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"display: inline-block; vertical-align: middle;\" class=\"" + STYLE_WEB_PROTEGE_ANNOTATED_RADIO_BUTTON_LABEL + "\">");
        sb.append("<div class=\"" + STYLE_WEB_PROTEGE_ANNOTATED_RADIO_BUTTON_MAIN_LABEL + "\">");
        sb.append(label);
        sb.append("</div>");
        sb.append("<div class=\"" + STYLE_WEB_PROTEGE_ANNOTATED_RADIO_BUTTON_DESCRIPTION_LABEL + "\">");
        sb.append(description);
        sb.append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

}

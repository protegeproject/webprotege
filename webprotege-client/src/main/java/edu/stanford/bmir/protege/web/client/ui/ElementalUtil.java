package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import elemental.client.Browser;
import elemental.dom.Element;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Mar 2018
 */
public class ElementalUtil {

    /**
     * Determines if the specified widget is active.
     * @param widget The widget.
     * @return true of the widget or a descendant of the widget is active, otherwise false.
     */
    public static boolean isWidgetOrDescendantWidgetActive(@Nonnull IsWidget widget) {
        Element activeElement = Browser.getDocument().getActiveElement();
        Element widgetElement = widget.asWidget().getElement().cast();
        while(activeElement != null) {
            if(activeElement.equals(widgetElement)) {
                return true;
            }
            activeElement = activeElement.getParentElement();
        }
        return false;
    }
}

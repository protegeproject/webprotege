package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
public class LayoutUtil {


    public static void setBounds(IsWidget widget, int top, int bottom, int left, int right) {
        Style style = widget.asWidget().getElement().getStyle();
        style.setPosition(Style.Position.ABSOLUTE);
        style.setTop(top, Style.Unit.PX);
        style.setBottom(bottom, Style.Unit.PX);
        style.setLeft(left, Style.Unit.PX);
        style.setRight(right, Style.Unit.PX);
    }

    public static void setFill(IsWidget widget, int top, int height) {
        Style style = widget.asWidget().getElement().getStyle();
        style.setPosition(Style.Position.ABSOLUTE);
        style.setTop(top, Style.Unit.PX);
        style.setHeight(height, Style.Unit.PX);
        style.setLeft(0, Style.Unit.PX);
        style.setRight(0, Style.Unit.PX);
    }
}

package edu.stanford.bmir.protege.web.client.tooltip;

import com.google.gwt.user.client.ui.IsWidget;
import elemental.client.Browser;
import elemental.dom.Element;
import elemental.html.HtmlElement;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class Tooltip {

    public Tooltip(Element reference,
                   TooltipOptions options) {
        // Native implementation
    }

    @JsOverlay
    public static Tooltip create(Element element,
                                 String title) {
        TooltipOptions options = new TooltipOptions();
        options.setTitle(title);
        options.setContainer(Browser.getDocument().getBody());
        return new Tooltip(element, options);
    }

    @JsOverlay
    public static Tooltip createOnRight(Element element,
                                 String title) {
        TooltipOptions options = new TooltipOptions();
        options.setTitle(title);
        options.setPlacement("right");
        options.setContainer(Browser.getDocument().getBody());
        return new Tooltip(element, options);
    }


    @JsOverlay
    public static Tooltip create(IsWidget widget,
                          TooltipOptions options) {
        return new Tooltip((Element) widget.asWidget().getElement(),
                           options);
    }

    @JsOverlay
    public static Tooltip create(IsWidget widget,
                          String title) {
        return create((Element) widget.asWidget().getElement(),
               title);
    }

    public native void show();

    public native void hide();

    public native void dispose();

    public native void toggle();

    public native void updateTitleContent(@Nonnull String titleContent);

    public native void updateTitleContent(@Nonnull HtmlElement titleContent);
}

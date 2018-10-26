package edu.stanford.bmir.protege.web.client.tooltip;

import elemental.dom.Element;
import elemental.html.HtmlElement;
import jsinterop.annotations.JsMethod;
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

    public native void show();

    public native void hide();

    public native void dispose();

    public native void toggle();

    public native void updateTitleContent(@Nonnull String titleContent);

    public native void updateTitleContent(@Nonnull HtmlElement titleContent);
}

package edu.stanford.bmir.protege.web.client.tooltip;

import elemental.dom.Element;
import elemental.html.HtmlElement;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Oct 2018
 */
@JsType
public class TooltipOptions {

    @JsProperty
    public native void setContainer(@SuppressWarnings("unusable-by-js") @Nullable Element container);

    @Nullable
    @JsProperty
    @SuppressWarnings("unusable-by-js")
    public native Element getContainer();


    @JsProperty
    public native void setHtml(boolean html);

    @JsProperty
    public native boolean isHtml();


    @JsProperty
    public native void setDelay(int delayMs);

    @JsProperty
    public native int getDelay();


    @JsProperty
    public native void setTitle(@Nonnull String title);

    @JsProperty
    public native String getTitle();

    @JsProperty
    public native void setPlacement(String right);

    @JsProperty
    public native void setTrigger(String click);
}

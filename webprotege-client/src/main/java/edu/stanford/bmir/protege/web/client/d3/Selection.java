package edu.stanford.bmir.protege.web.client.d3;

import elemental.dom.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Oct 2018
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Selection")
public class Selection {

    @JsMethod
    public native void call(Object function);

    @JsMethod
    public native void filter(@Nonnull Predicate<Boolean> filter);

    @JsMethod
    public native Selection select(@Nonnull String selector);

    @JsMethod
    public native Selection selectAll(@Nonnull String selector);

    @JsMethod
    public native Selection attr(@Nonnull String attribute, @Nonnull String value);

    @JsMethod
    public native Selection style(@Nonnull String property, @Nonnull String value);

    @JsMethod
    public native Selection merge(@Nonnull Selection otherSelection);

    @JsMethod
    public native Selection classed(@Nonnull String classNames, boolean classed);

    @JsMethod
    public native Selection property(@Nonnull String name, @Nonnull String value);

    @JsMethod
    public native Selection text(@Nonnull String text);

    @JsMethod
    public native Selection html(@Nonnull String text);

    @JsMethod
    public native void remove();
}

package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.dom.client.Element;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Oct 2018
 */
public class DownloadSvg {

    public void save(@Nonnull Element element, @Nonnull String fileName) {

        downloadSvg(element, fileName);
    }

    @JsMethod(namespace = JsPackage.GLOBAL)
    private static native void downloadSvg(Element element, String fileName);
}

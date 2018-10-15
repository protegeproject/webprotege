package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;
import elemental.dom.DOMTokenList;
import elemental.dom.Element;
import elemental.dom.Node;
import elemental.html.HTMLCollection;
import elemental.stylesheets.StyleSheet;
import elemental.stylesheets.StyleSheetList;
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
        process(element);
        downloadSvg(element, fileName);
    }

    private void process(Element element) {
        DOMTokenList classNames = element.getClassList();
        CSSStyleDeclaration computedStyle = Browser.getWindow().getComputedStyle(element, null);
        for(int i = 0; i < computedStyle.getLength(); i++) {
            String item = computedStyle.item(i);
            if (!computedStyle.isPropertyImplicit(item)) {
                GWT.log("[DownloadSvg] Item: " + item);
            }
        }
//        GWT.log("[DownloadSvg]" + computedStyle.getCssText());
        HTMLCollection children = element.getChildren();
        for(int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if(node instanceof Element) {
                process((Element) node);
            }
        }
    }

    @JsMethod(namespace = JsPackage.GLOBAL)
    private static native void downloadSvg(Element element, String fileName);
}

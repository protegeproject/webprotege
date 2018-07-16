package edu.stanford.bmir.protege.web.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jul 2018
 *
 * Tracks the scroll position of a widget and preserves and restores the scroll
 * position on attach and detach.
 */
public class ScrollTracker {

    @Nonnull
    private final IsWidget widget;

    private int scrollLeft = -1;

    private int scrollTop = -1;

    private HandlerRegistration handlerRegistration = () -> {};

    public ScrollTracker(@Nonnull IsWidget widget) {
        this.widget = checkNotNull(widget);
    }

    public void start() {
        handlerRegistration.removeHandler();
        handlerRegistration = widget.asWidget().addAttachHandler(event -> {
            if(event.isAttached()) {
                handleAttach();
            }
            else {
                handleDetach();
            }
        });
    }

    private void handleAttach() {
        Widget w = widget.asWidget();
        Element element = widget.asWidget().getElement();
        if(element == null) {
            return;
        }
        if(scrollLeft != -1) {
            element.setScrollLeft(scrollLeft);
        }
        if(scrollTop != -1) {
            element.setScrollTop(scrollTop);
        }
    }

    private void handleDetach() {
        Element element = widget.asWidget().getElement();
        if (element == null) {
            return;
        }
        scrollLeft = element.getScrollLeft();
        scrollTop = element.getScrollTop();
    }

}

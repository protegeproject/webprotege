package edu.stanford.bmir.protege.web.client.ui.library.popupmenu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public abstract class PopupMenuItem {

    private SafeHtml html;

    protected PopupMenuItem(SafeHtml html) {
        this.html = html;
    }

    public SafeHtml getHtml() {
        return html;
    }

    public abstract void handleClicked(ClickEvent clickEvent);

}

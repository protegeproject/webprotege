package edu.stanford.bmir.protege.web.shared.renderer;

import com.google.gwt.safehtml.shared.SafeHtml;
import org.semanticweb.owlapi.model.OWLObject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public interface HasHtmlBrowserText {

    SafeHtml getHtmlBrowserText(OWLObject object);
}

package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetEntityAnnotationsResult implements Result, HasBrowserTextMap {

    private List<OWLAnnotation> annotations;

    private BrowserTextMap browserTextMap;

    private GetEntityAnnotationsResult() {
    }

    public GetEntityAnnotationsResult(List<OWLAnnotation> annotations, BrowserTextMap browserTextMap) {
        this.annotations = annotations;
        this.browserTextMap = browserTextMap;
    }

    public List<OWLAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}

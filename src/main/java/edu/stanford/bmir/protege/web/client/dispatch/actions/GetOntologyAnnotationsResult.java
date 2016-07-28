package edu.stanford.bmir.protege.web.client.dispatch.actions;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.HasBrowserTextMap;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28 Jul 16
 */
public class GetOntologyAnnotationsResult implements Result, HasBrowserTextMap {

    private ImmutableList<OWLAnnotation> annotations;

    private BrowserTextMap browserTextMap;

    /**
     * For serialization only
     */
    private GetOntologyAnnotationsResult() {
    }

    public GetOntologyAnnotationsResult(ImmutableList<OWLAnnotation> annotations, BrowserTextMap browserTextMap) {
        this.annotations = checkNotNull(annotations);
        this.browserTextMap = checkNotNull(browserTextMap);
    }

    @Nonnull
    public ImmutableList<OWLAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public BrowserTextMap getBrowserTextMap() {
        return browserTextMap;
    }
}

package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRenderer;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

@FormDataBuilderSession
public class FormDataBuilderSessionRenderer {

    @Nonnull
    private final FrameComponentSessionRenderer frameComponentSessionRenderer;

    @Inject
    public FormDataBuilderSessionRenderer(@Nonnull FrameComponentSessionRenderer frameComponentSessionRenderer) {
        this.frameComponentSessionRenderer = frameComponentSessionRenderer;
    }

    @Nonnull
    public OWLEntityData getEntityRendering(OWLEntity subject) {
        return frameComponentSessionRenderer.getEntityRendering(subject);
    }

    @Nonnull
    public Collection<OWLEntityData> getRendering(IRI iri) {
        return frameComponentSessionRenderer.getRendering(iri);
    }
}

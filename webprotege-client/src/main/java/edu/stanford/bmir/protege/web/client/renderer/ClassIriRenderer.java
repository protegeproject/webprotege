package edu.stanford.bmir.protege.web.client.renderer;

import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Jun 2018
 */
public interface ClassIriRenderer {

    void renderClassIri(@Nonnull IRI iri,
                        @Nonnull Consumer<OWLClassData> renderingConsumer);
}

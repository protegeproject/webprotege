package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.IsWidget;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public interface IriEqualsView extends IsWidget {

    @Nonnull
    Optional<IRI> getIri();

    void setIri(IRI iri);
}

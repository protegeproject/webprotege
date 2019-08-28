package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
public interface AxiomChange extends OntologyChange {

    @Nonnull
    OWLAxiom getAxiom();

    @Override
    default OWLAxiom getAxiomOrThrow() throws NoSuchElementException {
        return getAxiom();
    }

    @Override
    default boolean isAxiomChange() {
        return true;
    }

    @Nonnull
    @Override
    default Set<OWLEntity> getSignature() {
        return getAxiom().getSignature();
    }
}

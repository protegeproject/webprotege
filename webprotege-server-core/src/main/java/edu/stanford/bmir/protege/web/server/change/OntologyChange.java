package edu.stanford.bmir.protege.web.server.change;

import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
public interface OntologyChange {

    @Nonnull
    OWLOntologyID getOntologyId();

    @Nonnull
    Set<OWLEntity> getSignature();

    default boolean isChangeFor(@Nonnull AxiomType<? extends OWLAxiom> axiomType) {
        return isAxiomChange() && getAxiomOrThrow().getAxiomType().equals(axiomType);
    }

    default boolean isAxiomChange() {
        return false;
    }

    default boolean isAddAxiom() {
        return false;
    }

    default boolean isRemoveAxiom() {
        return false;
    }

    /**
     * Gets the axiom that is the object of this ontology change.  This
     * method should only be called if isAxiomChange returns true.
     * @throws NoSuchElementException if this is not an axiom change
     */
    default OWLAxiom getAxiomOrThrow() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Nonnull
    OntologyChange replaceIris(@Nonnull OWLObjectDuplicator duplicator);

    @Nonnull
    OWLOntologyChangeRecord toOwlOntologyChangeRecord();

    default boolean isRemoveOntologyAnnotation() {
        return false;
    }

    default boolean isAddOntologyAnnotation() {
        return false;
    }

    void accept(@Nonnull OntologyChangeVisitor visitor);

    <R> R accept(@Nonnull OntologyChangeVisitorEx<R> visitorEx);
}

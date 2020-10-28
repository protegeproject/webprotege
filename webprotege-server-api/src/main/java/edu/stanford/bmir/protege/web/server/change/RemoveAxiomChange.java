package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
@AutoValue
public abstract class RemoveAxiomChange implements AxiomChange {

    public static RemoveAxiomChange of(@Nonnull OntologyDocumentId ontologyId,
                                       @Nonnull OWLAxiom axiom) {
        return new AutoValue_RemoveAxiomChange(ontologyId, axiom);
    }

    @Override
    public boolean isRemoveAxiom() {
        return true;
    }

    @Nonnull
    @Override
    public RemoveAxiomChange replaceIris(@Nonnull IriReplacer iriReplacer) {
        OWLAxiom duplicatedAxiom = iriReplacer.replaceIris(getAxiom());
        return RemoveAxiomChange.of(getOntologyDocumentId(), duplicatedAxiom);
    }

    @Nonnull
    @Override
    public RemoveAxiomChange replaceOntologyId(@Nonnull OntologyDocumentId ontologyId) {
        if(getOntologyDocumentId().equals(ontologyId)) {
            return this;
        }
        else {
            return RemoveAxiomChange.of(ontologyId, getAxiom());
        }
    }

    @Nonnull
    @Override
    public AddAxiomChange getInverseChange() {
        return AddAxiomChange.of(getOntologyDocumentId(), getAxiom());
    }

    @Override
    public void accept(@Nonnull OntologyChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull OntologyChangeVisitorEx<R> visitorEx) {
        return visitorEx.visit(this);
    }
}

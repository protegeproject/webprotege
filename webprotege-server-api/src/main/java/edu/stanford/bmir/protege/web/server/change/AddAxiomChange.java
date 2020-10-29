package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.util.IriReplacer;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
@AutoValue
public abstract class AddAxiomChange implements AxiomChange {

    public static AddAxiomChange of(@Nonnull OntologyDocumentId ontologyId,
                                    @Nonnull OWLAxiom axiom) {
        return new AutoValue_AddAxiomChange(ontologyId, axiom);
    }

    @Override
    public boolean isAddAxiom() {
        return true;
    }

    @Nonnull
    @Override
    public AddAxiomChange replaceIris(@Nonnull IriReplacer iriReplacer) {
        OWLAxiom duplicatedAxiom = iriReplacer.replaceIris(getAxiom());
        return AddAxiomChange.of(getOntologyDocumentId(), duplicatedAxiom);
    }

    @Nonnull
    @Override
    public AddAxiomChange replaceOntologyId(@Nonnull OntologyDocumentId ontologyId) {
        if(ontologyId.equals(getOntologyDocumentId())) {
            return this;
        }
        else {
            return AddAxiomChange.of(ontologyId, getAxiom());
        }
    }

    @Override
    public void accept(@Nonnull OntologyChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <R> R accept(@Nonnull OntologyChangeVisitorEx<R> visitorEx) {
        return visitorEx.visit(this);
    }

    @Nonnull
    @Override
    public RemoveAxiomChange getInverseChange() {
        return RemoveAxiomChange.of(getOntologyDocumentId(), getAxiom());
    }
}

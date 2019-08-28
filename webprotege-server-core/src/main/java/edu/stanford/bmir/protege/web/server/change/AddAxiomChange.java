package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.value.AutoValue;
import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-26
 */
@AutoValue
public abstract class AddAxiomChange implements AxiomChange {

    public static AddAxiomChange of(@Nonnull OWLOntologyID ontologyId,
                                    @Nonnull OWLAxiom axiom) {
        return new AutoValue_AddAxiomChange(ontologyId, axiom);
    }

    @Override
    public boolean isAddAxiom() {
        return true;
    }

    @Nonnull
    @Override
    public AddAxiomChange replaceIris(@Nonnull OWLObjectDuplicator duplicator) {
        OWLAxiom duplicatedAxiom = duplicator.duplicateObject(getAxiom());
        return AddAxiomChange.of(getOntologyId(), duplicatedAxiom);
    }

    @Nonnull
    @Override
    public OWLOntologyChangeRecord toOwlOntologyChangeRecord() {
        return new OWLOntologyChangeRecord(getOntologyId(), new AddAxiomData(getAxiom()));
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

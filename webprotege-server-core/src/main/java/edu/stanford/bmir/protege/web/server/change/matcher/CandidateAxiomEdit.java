package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeVisitor;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class CandidateAxiomEdit<A extends OWLAxiom> {

    private final AxiomType<A> axiomType;

    private Optional<A> addAxiom = Optional.empty();

    private Optional<A> removeAxiom = Optional.empty();



    public CandidateAxiomEdit(OntologyChange change0, OntologyChange change1, final AxiomType<A> axiomType) {
        this.axiomType = axiomType;
        processChanges(change0, change1);
    }

    @SuppressWarnings("unchecked")
    private void processChanges(OntologyChange change0, OntologyChange change1) {
        var visitor = new OntologyChangeVisitor() {
            @Override
            public void visit(@Nonnull AddAxiomChange addAxiomChange) {
                var axiom = addAxiomChange.getAxiom();
                if (axiom.getAxiomType().equals(axiomType)) {
                    addAxiom = Optional.of((A) axiom);
                }
            }

            @Override
            public void visit(@Nonnull RemoveAxiomChange removeAxiomChange) {
                OWLAxiom axiom = removeAxiomChange.getAxiom();
                if(axiom.getAxiomType().equals(axiomType)) {
                    removeAxiom = Optional.of((A) axiom);
                }
            }
        };
        change0.accept(visitor);
        change1.accept(visitor);
    }

    public Optional<A> getAddAxiom() {
        return addAxiom;
    }

    public Optional<A> getRemoveAxiom() {
        return removeAxiom;
    }

    public boolean hasAddAndRemove() {
        return addAxiom.isPresent() && removeAxiom.isPresent();
    }
}

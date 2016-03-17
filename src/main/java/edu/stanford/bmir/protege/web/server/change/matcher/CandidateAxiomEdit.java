package edu.stanford.bmir.protege.web.server.change.matcher;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public class CandidateAxiomEdit<A extends OWLAxiom> {

    private final AxiomType<A> axiomType;

    private Optional<A> addAxiom = Optional.empty();

    private Optional<A> removeAxiom = Optional.empty();



    public CandidateAxiomEdit(OWLOntologyChange change0, OWLOntologyChange change1, final AxiomType<A> axiomType) {
        this.axiomType = axiomType;
        processChanges(change0, change1, axiomType);
    }

    @SuppressWarnings("unchecked")
    private void processChanges(OWLOntologyChange change0, OWLOntologyChange change1, final AxiomType<A> axiomType) {
        OWLOntologyChangeVisitorAdapter visitor = new OWLOntologyChangeVisitorAdapter() {
            @Override
            public void visit(RemoveAxiom change) {
                if(change.getAxiom().getAxiomType().equals(axiomType)) {
                    removeAxiom = Optional.of((A) change.getAxiom());
                }
            }

            @Override
            public void visit(AddAxiom change) {
                if (change.getAxiom().getAxiomType().equals(axiomType)) {
                    addAxiom = Optional.of((A) change.getAxiom());
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

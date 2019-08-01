package edu.stanford.bmir.protege.web.server.change.matcher;

import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLOntologyChangeVisitorAdapter;

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



    public CandidateAxiomEdit(OWLOntologyChangeData change0, OWLOntologyChangeData change1, final AxiomType<A> axiomType) {
        this.axiomType = axiomType;
        processChanges(change0, change1, axiomType);
    }

    @SuppressWarnings("unchecked")
    private void processChanges(OWLOntologyChangeData change0, OWLOntologyChangeData change1, final AxiomType<A> axiomType) {
        OWLOntologyChangeDataVisitor<Void, RuntimeException> visitor = new OWLOntologyChangeDataVisitor<Void, RuntimeException>() {
            @Nonnull
            @Override
            public Void visit(AddAxiomData data) throws RuntimeException {
                if (data.getAxiom().getAxiomType().equals(axiomType)) {
                    addAxiom = Optional.of((A) data.getAxiom());
                }
                return null;
            }

            @Override
            public Void visit(RemoveAxiomData data) throws RuntimeException {
                if(data.getAxiom().getAxiomType().equals(axiomType)) {
                    removeAxiom = Optional.of((A) data.getAxiom());
                }
                return null;
            }

            @Override
            public Void visit(AddOntologyAnnotationData data) throws RuntimeException {
                return null;
            }

            @Override
            public Void visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return null;
            }

            @Override
            public Void visit(SetOntologyIDData data) throws RuntimeException {
                return null;
            }

            @Override
            public Void visit(AddImportData data) throws RuntimeException {
                return null;
            }

            @Override
            public Void visit(RemoveImportData data) throws RuntimeException {
                return null;
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

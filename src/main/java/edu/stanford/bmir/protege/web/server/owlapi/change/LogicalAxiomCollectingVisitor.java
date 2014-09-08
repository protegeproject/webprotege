package edu.stanford.bmir.protege.web.server.owlapi.change;

import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;

import java.util.Collection;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 07/09/2014
 */
public class LogicalAxiomCollectingVisitor implements OWLOntologyChangeDataVisitor<Void, RuntimeException> {

    private static final Void VOID = null;

    private Collection<OWLLogicalAxiom> axioms;

    public LogicalAxiomCollectingVisitor(Collection<OWLLogicalAxiom> axioms) {
        this.axioms = axioms;
    }

    @Override
    public Void visit(AddAxiomData data) throws RuntimeException {
        if (data.getAxiom().isLogicalAxiom()) {
            axioms.add((OWLLogicalAxiom) data.getAxiom());
        }
        return VOID;
    }

    @Override
    public Void visit(RemoveAxiomData data) throws RuntimeException {
        if (data.getAxiom().isLogicalAxiom()) {
            axioms.remove((OWLLogicalAxiom) data.getAxiom());
        }
        return VOID;
    }

    @Override
    public Void visit(AddOntologyAnnotationData data) throws RuntimeException {
        return VOID;
    }

    @Override
    public Void visit(RemoveOntologyAnnotationData data) throws RuntimeException {
        return VOID;
    }

    @Override
    public Void visit(SetOntologyIDData data) throws RuntimeException {
        return VOID;
    }

    @Override
    public Void visit(AddImportData data) throws RuntimeException {
        return VOID;
    }

    @Override
    public Void visit(RemoveImportData data) throws RuntimeException {
        return VOID;
    }
}

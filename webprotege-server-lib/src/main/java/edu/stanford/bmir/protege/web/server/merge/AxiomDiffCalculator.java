package edu.stanford.bmir.protege.web.server.merge;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.merge.Diff;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/01/15
 */
public class AxiomDiffCalculator {

    public Diff<OWLAxiom> computeDiff(OWLOntology from, OWLOntology to) {
        ImmutableSet.Builder<OWLAxiom> addedAxioms = ImmutableSet.builder();
        ImmutableSet.Builder<OWLAxiom> removedAxioms = ImmutableSet.builder();
        for(OWLAxiom ax : to.getAxioms()) {
            if(!from.containsAxiom(ax)) {
                addedAxioms.add(ax);
            }
        }
        for(OWLAxiom ax : from.getAxioms()) {
            if(!to.containsAxiom(ax)) {
                removedAxioms.add(ax);
            }
        }
        return new Diff<>(addedAxioms.build(), removedAxioms.build());
    }
}

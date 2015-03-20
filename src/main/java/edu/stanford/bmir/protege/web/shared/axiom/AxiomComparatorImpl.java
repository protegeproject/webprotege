package edu.stanford.bmir.protege.web.shared.axiom;

import com.google.common.collect.Ordering;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class AxiomComparatorImpl implements Comparator<OWLAxiom> {

    private Comparator<OWLAxiom> compoundComparator;

    @Inject
    public AxiomComparatorImpl(AxiomBySubjectComparator axiomBySubjectComparator, AxiomByTypeComparator axiomByTypeComparator, AxiomByRenderingComparator axiomByRenderingComparator) {
        compoundComparator = Ordering.compound(
                Arrays.asList(
                        checkNotNull(axiomBySubjectComparator),
                        checkNotNull(axiomByTypeComparator),
                        checkNotNull(axiomByRenderingComparator)));
    }

    @Override
    public int compare(OWLAxiom o1, OWLAxiom o2) {
        return compoundComparator.compare(o1, o2);
    }
}

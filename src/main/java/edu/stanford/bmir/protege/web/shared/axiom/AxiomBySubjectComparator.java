package edu.stanford.bmir.protege.web.shared.axiom;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 03/02/15
 *
 * A comparator that compares axioms by a rendering of their subject.
 */
public class AxiomBySubjectComparator implements Comparator<OWLAxiom> {


    public static final int BEFORE = -1;
    public static final int SAME = 0;
    public static final int AFTER = 1;

    private AxiomSubjectProvider axiomSubjectProvider;

    private Comparator<OWLObject> subjectComparator;

    @Inject
    public AxiomBySubjectComparator(AxiomSubjectProvider axiomSubjectProvider, Comparator<OWLObject> subjectComparator) {
        this.axiomSubjectProvider = checkNotNull(axiomSubjectProvider);
        this.subjectComparator = checkNotNull(subjectComparator);
    }

    /**
     * Compares the two axioms using their subject.
     * @param ax1 The first axiom.
     * @param ax2 The second axiom.
     * @return If both axioms have subjects: An integer representing the comparison of the subjects of the two axioms.
     * If the first axiom has a subject, but the second axiom does not, then -1 (first before second).  If the second axiom has a subject
     * but the first axiom does not, then 1 (first after second).  If neither the first axiom has a subject or the second
     * axiom has a subject then 0.
     */
    @Override
    public int compare(OWLAxiom ax1, OWLAxiom ax2) {
        Optional<? extends OWLObject> optionalSubject1 = axiomSubjectProvider.getSubject(ax1);
        Optional<? extends OWLObject> optionalSubject2 = axiomSubjectProvider.getSubject(ax2);
        if(optionalSubject1.isPresent()) {
            if(optionalSubject2.isPresent()) {
                OWLObject subject1 = optionalSubject1.get();
                OWLObject subject2 = optionalSubject2.get();
                return subjectComparator.compare(subject1, subject2);
            }
            else {
                return BEFORE;
            }
        }
        else {
            if(optionalSubject2.isPresent()) {
                return AFTER;
            }
            else {
                return SAME;
            }
        }
    }
}

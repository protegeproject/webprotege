package edu.stanford.bmir.protege.web.shared.usage;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Comparator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class UsageReferenceComparator implements Comparator<UsageReference> {

    private OWLEntity subject;

    public UsageReferenceComparator(OWLEntity subject) {
        this.subject = subject;
    }

    @Override
    public int compare(UsageReference o1, UsageReference o2) {
        if(o1.getAxiomSubject().equals(Optional.of(subject))) {
            if(!o2.getAxiomSubject().equals(Optional.of(subject))) {
                return -1;
            }
        }
        else if(o2.getAxiomSubject().equals(Optional.of(subject))) {
            return 1;
        }
        if(o1.getAxiomSubject().isPresent()) {
            if(!o2.getAxiomSubject().isPresent()) {
                return -1;
            }
        }
        else if(o2.getAxiomSubject().isPresent()) {
            return 1;
        }

        int subjectRenderingDiff = o1.getSubjectRendering().compareTo(o2.getSubjectRendering());
        if(subjectRenderingDiff != 0) {
            return subjectRenderingDiff;
        }

        int typeDiff = o1.getAxiomType().getIndex() - o2.getAxiomType().getIndex();
        if(typeDiff != -1) {
            return typeDiff;
        }

        return o1.getAxiomRendering().compareTo(o2.getAxiomRendering());
    }
}

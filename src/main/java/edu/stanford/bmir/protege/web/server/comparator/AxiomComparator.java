package edu.stanford.bmir.protege.web.server.comparator;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomIRISubjectProvider;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.util.IRIShortFormProvider;

import java.util.Comparator;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
public class AxiomComparator implements Comparator<OWLAxiom> {

    private final ImmutableMap<AxiomType<?>, Integer> typeIndexMap;

    private IRIShortFormProvider iriShortFormProvider;

    private AxiomIRISubjectProvider subjectProvider;

    public AxiomComparator(AxiomIRISubjectProvider subjectProvider,  IRIShortFormProvider iriShortFormProvider, List<AxiomType<?>> axiomTypeOrdering) {
        this.subjectProvider = subjectProvider;
        ImmutableMap.Builder<AxiomType<?>, Integer> builder = ImmutableMap.builder();
        int index = 0;
        for(AxiomType<?> axiomType : axiomTypeOrdering) {
            builder.put(axiomType, index);
            index++;
        }
        typeIndexMap = builder.build();
        this.iriShortFormProvider = iriShortFormProvider;
    }

    @Override
    public int compare(OWLAxiom o1, OWLAxiom o2) {
        int subjectDiff = getSubjectDiff(o1, o2);
        if(subjectDiff != 0) {
            return subjectDiff;
        }
        int type1 = typeIndexMap.get(o1.getAxiomType());
        int type2 = typeIndexMap.get(o2.getAxiomType());
        int typeDiff = type1 - type2;
        if(typeDiff != 0) {
            return typeDiff;
        }
        return o1.compareTo(o2);
    }

    private int getSubjectDiff(OWLAxiom o1, OWLAxiom o2) {
        Optional<IRI> subject1 = subjectProvider.getSubject(o1);
        Optional<IRI> subject2 = subjectProvider.getSubject(o2);
        int diff;
        if(subject1.isPresent()) {
            if(subject2.isPresent()) {
                String shortForm1 = iriShortFormProvider.getShortForm(subject1.get());
                String shortForm2 = iriShortFormProvider.getShortForm(subject2.get());
                diff = shortForm1.compareToIgnoreCase(shortForm2);
            }
            else {
                diff = -1;
            }
        }
        else {
            if(subject2.isPresent()) {
                diff = 1;
            }
            else {
                diff = 0;
            }
        }
        return diff;
    }
}

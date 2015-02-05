package edu.stanford.bmir.protege.web.shared.object;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLObject;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class SWRLAtomSelector implements OWLObjectSelector<SWRLAtom> {

    private Comparator<? super SWRLAtom> swrlObjectComparator;


    public SWRLAtomSelector(Comparator<? super SWRLAtom> swrlObjectComparator) {
        this.swrlObjectComparator = swrlObjectComparator;
    }

    @Override
    public Optional<SWRLAtom> selectOne(Iterable<SWRLAtom> objects) {
        Optional<SWRLAtom> result = Optional.absent();
        for(SWRLAtom atom : objects) {
            if(result.isPresent()) {
                if(swrlObjectComparator.compare(atom, result.get()) < 0) {
                    result = Optional.of(atom);
                }
            }
            else {
                result = Optional.of(atom);
            }
        }
        return result;
    }
}

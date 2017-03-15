package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.model.SWRLAtom;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public class SWRLAtomSelector implements OWLObjectSelector<SWRLAtom> {

    private Comparator<? super SWRLAtom> swrlObjectComparator;


    @Inject
    public SWRLAtomSelector(Comparator<? super SWRLAtom> swrlObjectComparator) {
        this.swrlObjectComparator = swrlObjectComparator;
    }

    @Override
    public Optional<SWRLAtom> selectOne(Iterable<SWRLAtom> objects) {
        Optional<SWRLAtom> result = Optional.empty();
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

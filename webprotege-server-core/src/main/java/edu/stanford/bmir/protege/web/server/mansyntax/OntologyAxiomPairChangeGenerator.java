package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@SuppressWarnings("ConstantConditions")
public class OntologyAxiomPairChangeGenerator {

    @Inject
    public OntologyAxiomPairChangeGenerator() {
    }

    public List<OntologyChange> generateChanges(Set<OntologyAxiomPair> fromPairs, Set<OntologyAxiomPair> toPairs) {
        List<OntologyChange> result = Lists.newArrayList();
        for(OntologyAxiomPair fromPair : fromPairs) {
            if(!toPairs.contains(fromPair)) {
                result.add(RemoveAxiomChange.of(checkNotNull(fromPair.getOntology().getOntologyID()), fromPair.getAxiom()));
            }
        }
        for(OntologyAxiomPair toPair : toPairs) {
            if(!fromPairs.contains(toPair)) {
                result.add(AddAxiomChange.of(checkNotNull(toPair.getOntology().getOntologyID()), toPair.getAxiom()));
            }
        }
        return result;
    }
}

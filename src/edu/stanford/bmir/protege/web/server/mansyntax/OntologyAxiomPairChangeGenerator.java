package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.common.collect.Lists;
import org.coode.owlapi.manchesterowlsyntax.OntologyAxiomPair;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveAxiom;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
public class OntologyAxiomPairChangeGenerator {

    public List<OWLOntologyChange> generateChanges(Set<OntologyAxiomPair> fromPairs, Set<OntologyAxiomPair> toPairs) {
        List<OWLOntologyChange> result = Lists.newArrayList();
        for(OntologyAxiomPair fromPair : fromPairs) {
            if(!toPairs.contains(fromPair)) {
                result.add(new RemoveAxiom(checkNotNull(fromPair.getOntology()), fromPair.getAxiom()));
            }
        }
        for(OntologyAxiomPair toPair : toPairs) {
            if(!fromPairs.contains(toPair)) {
                result.add(new AddAxiom(checkNotNull(toPair.getOntology()), toPair.getAxiom()));
            }
        }
        return result;
    }
}

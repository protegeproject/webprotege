package edu.stanford.bmir.protege.web.server.mansyntax;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
public class ManchesterSyntaxChangeGenerator {

    private ManchesterSyntaxFrameParser parser;

    @Inject
    public ManchesterSyntaxChangeGenerator(
            ManchesterSyntaxFrameParser parser) {
        this.parser = parser;
    }

    /**
     * Generates a list of ontology changes to convert one piece of Manchester Syntax to another piece.
     * @param from The from piece of syntax.  Not {@code null}.
     * @param to The to piece of syntax.  Not {@code null}.
     * @return A list of changes.  May be empty of the to pieces of syntax encode the same axioms in the same ontology.
     * @throws ParserException If there was a problem parsing either {@code from} or {@code to}.
     */
    public List<OWLOntologyChange> generateChanges(String from, String to, HasFreshEntities hasFreshEntities) throws ParserException {
        Set<OntologyAxiomPair> toPairs = getOntologyAxiomPairs(checkNotNull(to), checkNotNull(hasFreshEntities));
        Set<OntologyAxiomPair> fromPairs = getOntologyAxiomPairs(checkNotNull(from), checkNotNull(hasFreshEntities));
        OntologyAxiomPairChangeGenerator pairsChangeGenerator = new OntologyAxiomPairChangeGenerator();
        return pairsChangeGenerator.generateChanges(fromPairs, toPairs);
    }

    private Set<OntologyAxiomPair> getOntologyAxiomPairs(String rendering, HasFreshEntities hasFreshEntities) throws ParserException {
        return parser.parse(rendering, hasFreshEntities);
    }

}

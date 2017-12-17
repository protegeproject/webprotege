package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseError;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
@AutoFactory
public class ManchesterSyntaxChangeGenerator implements ChangeListGenerator<Optional<ManchesterSyntaxFrameParseError>> {

    private final ManchesterSyntaxFrameParser parser;

    private final OntologyAxiomPairChangeGenerator changeGenerator;

    private final String from;

    private final String to;

    private final HasFreshEntities hasFreshEntities;



    /**
     * Generates a list of ontology changes to convert one piece of Manchester Syntax to another piece.
     * @param from The from piece of syntax.  Not {@code null}.
     * @param to The to piece of syntax.  Not {@code null}.
     * @throws ParserException If there was a problem parsing either {@code from} or {@code to}.
     */
    @Inject
    public ManchesterSyntaxChangeGenerator(@Provided @Nonnull ManchesterSyntaxFrameParser parser,
                                           @Provided OntologyAxiomPairChangeGenerator changeGenerator,
                                           @Nonnull String from,
                                           @Nonnull String to,
                                           @Nonnull HasFreshEntities hasFreshEntities) {
        this.parser = parser;
        this.changeGenerator = changeGenerator;
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
        this.hasFreshEntities = checkNotNull(hasFreshEntities);
    }

    @Override
    public OntologyChangeList<Optional<ManchesterSyntaxFrameParseError>> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Optional<ManchesterSyntaxFrameParseError>> builder = OntologyChangeList.builder();
        Optional<ManchesterSyntaxFrameParseError> error;
        try {
            List<OWLOntologyChange> changes = generateChanges();
            error = Optional.empty();
            builder.addAll(changes);
        } catch (ParserException e) {
            ManchesterSyntaxFrameParseError parseError = ManchesterSyntaxFrameParser.getParseError(e);
            error = Optional.of(parseError);
        }
        return builder.build(error);
    }

    @Override
    public Optional<ManchesterSyntaxFrameParseError> getRenamedResult(Optional<ManchesterSyntaxFrameParseError> result, RenameMap renameMap) {
        return result;
    }


    private List<OWLOntologyChange> generateChanges() throws ParserException {
        Set<OntologyAxiomPair> toPairs = getOntologyAxiomPairs(to, hasFreshEntities);
        Set<OntologyAxiomPair> fromPairs = getOntologyAxiomPairs(from, hasFreshEntities);
        return changeGenerator.generateChanges(fromPairs, toPairs);
    }

    private Set<OntologyAxiomPair> getOntologyAxiomPairs(String rendering, HasFreshEntities hasFreshEntities) throws ParserException {
        return parser.parse(rendering, hasFreshEntities);
    }

}

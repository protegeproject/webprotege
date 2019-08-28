package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseError;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
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

    private final OWLEntityData subject;

    private final String from;

    private final String to;

    @Nonnull
    private final String commitMessage;

    private final HasFreshEntities hasFreshEntities;

    private final ReverseEngineeredChangeDescriptionGeneratorFactory factory;


    /**
     * Generates a list of ontology changes to convert one piece of Manchester Syntax to another piece.
     *
     * @param subject
     * @param from The from piece of syntax.  Not {@code null}.
     * @param to The to piece of syntax.  Not {@code null}.
     * @param commitMessage The commit message.  Can be empty.
     * @throws ParserException If there was a problem parsing either {@code from} or {@code to}.
     */
    @Inject
    public ManchesterSyntaxChangeGenerator(@Provided @Nonnull ManchesterSyntaxFrameParser parser,
                                           @Provided OntologyAxiomPairChangeGenerator changeGenerator,
                                           @Provided @Nonnull ReverseEngineeredChangeDescriptionGeneratorFactory factory,
                                           @Nonnull OWLEntityData subject,
                                           @Nonnull String from,
                                           @Nonnull String to,
                                           @Nonnull String commitMessage,
                                           @Nonnull HasFreshEntities hasFreshEntities) {
        this.parser = parser;
        this.changeGenerator = changeGenerator;
        this.subject = subject;
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
        this.commitMessage = commitMessage;
        this.hasFreshEntities = checkNotNull(hasFreshEntities);
        this.factory = factory;
    }

    @Override
    public OntologyChangeList<Optional<ManchesterSyntaxFrameParseError>> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Optional<ManchesterSyntaxFrameParseError>> builder = OntologyChangeList.builder();
        Optional<ManchesterSyntaxFrameParseError> error;
        try {
            List<OntologyChange> changes = generateChanges();
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


    private List<OntologyChange> generateChanges() throws ParserException {
        Set<OntologyAxiomPair> toPairs = getOntologyAxiomPairs(to, hasFreshEntities);
        Set<OntologyAxiomPair> fromPairs = getOntologyAxiomPairs(from, hasFreshEntities);
        return changeGenerator.generateChanges(fromPairs, toPairs);
    }

    private Set<OntologyAxiomPair> getOntologyAxiomPairs(String rendering, HasFreshEntities hasFreshEntities) throws ParserException {
        return parser.parse(rendering, hasFreshEntities);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Optional<ManchesterSyntaxFrameParseError>> result) {
        String changeDescription = "Edited description of " + subject.getBrowserText() + ".";
        if(!commitMessage.isEmpty()) {
            changeDescription += "\n" + commitMessage;
        }
        return changeDescription;
    }
}

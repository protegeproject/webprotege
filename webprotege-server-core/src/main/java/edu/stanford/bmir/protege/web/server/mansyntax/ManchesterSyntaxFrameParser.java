package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import edu.stanford.bmir.protege.web.shared.frame.ManchesterSyntaxFrameParseError;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.OWLOntologyChecker;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntax;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxFramesParser;
import org.semanticweb.owlapi.manchestersyntax.parser.ManchesterOWLSyntaxTokenizer;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.util.OntologyAxiomPair;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class ManchesterSyntaxFrameParser {

    private final OWLDataFactory dataFactory;

    private final OWLOntologyChecker ontologyChecker;

    private final DictionaryManager dictionaryManager;

    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Inject
    public ManchesterSyntaxFrameParser(OWLOntologyChecker ontologyChecker,
                                       OWLDataFactory dataFactory,
                                       DictionaryManager dictionaryManager,
                                       DefaultOntologyIdManager defaultOntologyIdManager) {
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.ontologyChecker = ontologyChecker;
        this.dataFactory = dataFactory;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
    }

    public Set<OntologyAxiomPair> parse(String syntax, HasFreshEntities hasFreshEntities) throws ParserException {
        OWLEntityChecker entityChecker = new WebProtegeOWLEntityChecker(
                hasFreshEntities,
                dictionaryManager
        );
        ManchesterOWLSyntaxFramesParser parser = new ManchesterOWLSyntaxFramesParser(dataFactory, entityChecker);
        parser.setOWLOntologyChecker(ontologyChecker);
        var defaultOntologyId = defaultOntologyIdManager.getDefaultOntologyId();
        parser.setDefaultOntology(new ShellOwlOntology(defaultOntologyId));
        return parser.parse(syntax);
    }

    public static ManchesterSyntaxFrameParseError getParseError(ParserException e) {
        List<EntityType<?>> expectedEntityTypes = getExpectedEntityTypes(e);
        String message = e.getMessage().replace(ManchesterOWLSyntaxTokenizer.EOF, "end of description");
        return new ManchesterSyntaxFrameParseError(message,
                e.getColumnNumber(),
                e.getLineNumber(),
                e.getCurrentToken(),
                expectedEntityTypes);
    }

    public static List<EntityType<?>> getExpectedEntityTypes(ParserException e) {
        String currentToken = e.getCurrentToken();
        if (isManchesterSyntaxKeyword(currentToken)) {
            return Collections.emptyList();
        }
        if(e.getCurrentToken().equals(ManchesterOWLSyntaxTokenizer.EOF)) {
            return Collections.emptyList();
        }
        List<EntityType<?>> expectedEntityTypes = Lists.newArrayList();
        if(e.isClassNameExpected()) {
            expectedEntityTypes.add(EntityType.CLASS);
        }
        if(e.isIndividualNameExpected()) {
            expectedEntityTypes.add(EntityType.NAMED_INDIVIDUAL);
        }
        if(e.isObjectPropertyNameExpected()) {
            expectedEntityTypes.add(EntityType.OBJECT_PROPERTY);
        }
        if(e.isDataPropertyNameExpected()) {
            expectedEntityTypes.add(EntityType.DATA_PROPERTY);
        }
        if(e.isAnnotationPropertyNameExpected()) {
            expectedEntityTypes.add(EntityType.ANNOTATION_PROPERTY);
        }
        if(e.isDatatypeNameExpected()) {
            expectedEntityTypes.add(EntityType.DATATYPE);
        }
        return expectedEntityTypes;
    }

    private static boolean isManchesterSyntaxKeyword(String currentToken) {
        String strippedToken;
        if(currentToken.endsWith(":")) {
            strippedToken = currentToken.substring(0, currentToken.length() - 1);
        }
        else {
            strippedToken = currentToken;
        }
        for(ManchesterOWLSyntax syntax : ManchesterOWLSyntax.values()) {
            if(strippedToken.equals(syntax.keyword())) {
                return true;
            }
        }
        return false;
    }


}

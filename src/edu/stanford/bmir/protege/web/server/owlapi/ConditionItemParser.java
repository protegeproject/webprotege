package edu.stanford.bmir.protege.web.server.owlapi;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class ConditionItemParser {

    private OWLAPIProject project;

    public ConditionItemParser(OWLAPIProject project) {
        this.project = project;
    }

    /**
     * Parses the text representing a condition item.  The text is assumed to use the short forms provided by the current
     * project short form provider, and is assumed to be in the Manchester Syntax.
     * @param conditionItemText The text to parse
     * @return The class expression that was parsed out of the text.
     * @throws ParserException If the text did not constitute a well formed class expression.  The exception contains
     * more information about what was expected.
     */
    public OWLClassExpression parse(String conditionItemText) throws ParserException {
        RenderingManager rm = project.getRenderingManager();
        BidirectionalShortFormProvider shortFormProvider = rm.getShortFormProvider();
        String trimmedText = conditionItemText.trim();
        ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(project.getDataFactory(), trimmedText);
        ShortFormEntityChecker entityChecker = new ShortFormEntityChecker(new EscapingShortFormProvider(shortFormProvider));
        parser.setOWLEntityChecker(entityChecker);
        return parser.parseClassExpression();
    }



}

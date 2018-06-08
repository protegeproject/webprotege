package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public class LiteralInLexicalSpaceMatcher implements Matcher<OWLLiteral> {

    @Override
    public boolean matches(@Nonnull OWLLiteral value) {
        OWLDatatype datatype = value.getDatatype();
        if(datatype.isBuiltIn()) {
            OWL2Datatype builtInDatatype = datatype.getBuiltInDatatype();
            Pattern pattern = builtInDatatype.getPattern();
            return pattern.matcher(value.getLiteral()).matches();
        }
        else {
            return true;
        }
    }
}

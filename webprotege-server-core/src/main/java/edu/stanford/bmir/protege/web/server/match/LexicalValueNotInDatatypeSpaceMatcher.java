package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2018
 */
public class LexicalValueNotInDatatypeSpaceMatcher implements Matcher<OWLLiteral> {

    @Override
    public boolean matches(@Nonnull OWLLiteral lit) {
        if (lit.getDatatype().isBuiltIn()) {
            OWL2Datatype datatype = lit.getDatatype().getBuiltInDatatype();
            return !datatype.getPattern().matcher(lit.getLiteral()).matches();
        }
        else {
            return false;
        }
    }
}

package edu.stanford.bmir.protege.web.server.legacy;

import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/04/2012
 * <p>
 *     Parses P3 encoded literals into proper literals.
 * </p>
 */
@Deprecated
public class P3LiteralParser {

    /**
     * P3 uses some weird (very weird) encoding for plain literals with language tags.  The literal "foo@en" would be encoded as
     * "~#en foo" (as far as I can tell).
     * Horrible.  There's a standard for this!
     */
    private static final String P3_LITERAL_ENCODING_PREFIX = "~#";

    private static final Pattern P3_LITERAL_ENCODING_PATTERN = Pattern.compile(Pattern.quote(P3_LITERAL_ENCODING_PREFIX) + "([^ ]*) " + "(.*)");

    private static final int LANG_TAG_GROUP = 1;

    private static final int LITERAL_TAG_GROUP = 2;

    
    
    private String p3Literal;

    private String lang;
    
    private String literal;
    
    public P3LiteralParser(String p3Literal) {
        this.p3Literal = p3Literal;
        parseLiteral();
    }

    public P3LiteralParser(OWLLiteral literal) {
        if(literal.isRDFPlainLiteral()) {
            this.literal = literal.getLiteral();
            this.lang = literal.getLang();
        }
        else {
            this.literal = literal.getLiteral();
            this.lang = "";
        }
    }
    
    private void parseLiteral() {
        Matcher matcher = P3_LITERAL_ENCODING_PATTERN.matcher(p3Literal);
        if(matcher.matches()) {
            lang = matcher.group(LANG_TAG_GROUP);
            literal = matcher.group(LITERAL_TAG_GROUP);
        }
        else {
            lang = "";
            literal = p3Literal;
        }
    }
    
    public String getLang() {
        return lang;
    }
    
    public String getLiteral() {
        return literal;
    }
    
    public String formatP3Literal() {
        if (!lang.isEmpty()) {
            return P3_LITERAL_ENCODING_PREFIX + lang + " " + literal;
        }
        else {
            return literal;
        }
    }
    
    public OWLLiteral getOWLLiteral(OWLDataFactory df) {
        return df.getOWLLiteral(literal, lang);
    }
    
    
}

package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class LiteralMatcher implements AnnotationValueMatcher<OWLLiteral> {

    @Nonnull
    private final Matcher<String> lexicalValueMatcher;

    @Nonnull
    private final Matcher<String> languageTagMatcher;

    @Nonnull
    private final Matcher<OWLDatatype> datatypeMatcher;

    public LiteralMatcher(@Nonnull Matcher<String> lexicalValueMatcher,
                          @Nonnull Matcher<String> languageTagMatcher,
                          @Nonnull Matcher<OWLDatatype> datatypeMatcher) {
        this.lexicalValueMatcher = checkNotNull(lexicalValueMatcher);
        this.languageTagMatcher = checkNotNull(languageTagMatcher);
        this.datatypeMatcher = checkNotNull(datatypeMatcher);
    }

    @Override
    public boolean matches(@Nonnull OWLLiteral literal) {
        return lexicalValueMatcher.matches(literal.getLiteral())
                && languageTagMatcher.matches(literal.getLang())
                && datatypeMatcher.matches(literal.getDatatype());
    }
}

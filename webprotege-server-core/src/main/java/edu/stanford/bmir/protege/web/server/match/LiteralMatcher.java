package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class LiteralMatcher implements Matcher<OWLLiteral> {

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

    public static LiteralMatcher forLexicalPattern(@Nonnull Pattern pattern) {
        return new LiteralMatcher(
                lexicalValue -> pattern.matcher(lexicalValue).find(),
                langTag -> true,
                datatype -> true
        );
    }

    public static Matcher<OWLLiteral> forPredicate(@Nonnull Predicate<String> predicate) {
        return new LiteralMatcher(
                predicate::test,
                langTag -> true,
                datatype -> true
        );
    }

    public static Matcher<OWLLiteral> forHasAnyLangTag() {
        return new LiteralMatcher(lexicalValue -> true,
                                  langTag -> !langTag.isEmpty(),
                                  datatype -> true);
    }

    public static Matcher<OWLLiteral> forLexicalValue(Matcher<String> lexicalValueMatcher) {
        return new LiteralMatcher(lexicalValueMatcher, langTag -> true, datatype -> true);
    }

    public static Matcher<OWLLiteral> forLangTag(Matcher<String> languageTagMatcher) {
        return new LiteralMatcher(Matcher.matchesAny(),
                                  languageTagMatcher,
                                  Matcher.matchesAny());
    }

    public static Matcher<OWLLiteral> forHasLangTag(@Nonnull String langTagMatch) {
        return new LiteralMatcher(lexicalValue -> true,
                                  langTagMatch::equalsIgnoreCase,
                                  datatype -> true);
    }


    public static Matcher<OWLLiteral> forNoLangTag() {
        return new LiteralMatcher(lexicalValue -> true,
                                  String::isEmpty,
                                  datatype -> true);
    }

    public static Matcher<OWLLiteral> forXsdBooleanTrue() {
        return new LiteralMatcher(lexicalValue -> lexicalValue.equals("true") || lexicalValue.equals("1"),
                                  String::isEmpty,
                                  OWLDatatype::isBoolean);
    }

    public static Matcher<OWLLiteral> forXsdBooleanFalse() {
        return new LiteralMatcher(lexicalValue -> !lexicalValue.equals("false") && !lexicalValue.equals("1"),
                                  String::isEmpty,
                                  OWLDatatype::isBoolean);
    }
}

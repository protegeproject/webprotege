package edu.stanford.bmir.protege.web.server.match;

import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Jun 2018
 */
public class IriLexicalValueMatcher implements Matcher<IRI> {

    @Nonnull
    private final Matcher<String> iriLexicalValueMatcher;


    public IriLexicalValueMatcher(@Nonnull Matcher<String> iriLexicalValueMatcher) {
        this.iriLexicalValueMatcher = checkNotNull(iriLexicalValueMatcher);
    }

    @Override
    public boolean matches(@Nonnull IRI value) {
        return iriLexicalValueMatcher.matches(value.toString());
    }
}

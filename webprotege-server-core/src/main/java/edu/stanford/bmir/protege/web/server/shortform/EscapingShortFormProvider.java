package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A short form provider which wraps a non-escaping short form provider to ensure that short forms with spaces
 * are surrounded in single quotes.
 */
public class EscapingShortFormProvider implements ShortFormProvider {

    private DictionaryManager delegate;

    @Inject
    public EscapingShortFormProvider(@Nonnull DictionaryManager delegate) {
        this.delegate = checkNotNull(delegate);
    }

    private String getStripped(String s) {
        String stripped;
        if(s.startsWith("'") && s.endsWith("'")) {
            stripped = s.substring(1, s.length() - 1);
        }
        else {
            stripped = s;
        }
        return stripped;
    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull OWLEntity entity) {
        return ShortFormQuotingUtils.getQuotedShortForm(delegate.getShortForm(entity));
    }

    @Override
    public void dispose() {

    }
}

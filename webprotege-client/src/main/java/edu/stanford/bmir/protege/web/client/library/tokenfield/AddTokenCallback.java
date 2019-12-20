package edu.stanford.bmir.protege.web.client.library.tokenfield;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-14
 */
public interface AddTokenCallback<T> {
    void addToken(T tokenObject, String label);
}

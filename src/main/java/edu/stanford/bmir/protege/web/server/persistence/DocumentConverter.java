package edu.stanford.bmir.protege.web.server.persistence;

import org.bson.Document;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public interface DocumentConverter<T> {

    Document toDocument(T object);

    T fromDocument(Document document);
}

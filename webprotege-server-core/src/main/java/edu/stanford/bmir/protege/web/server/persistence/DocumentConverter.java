package edu.stanford.bmir.protege.web.server.persistence;

import org.bson.Document;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public interface DocumentConverter<T> {

    Document toDocument(@Nonnull  T object);

    T fromDocument(@Nonnull Document document);
}

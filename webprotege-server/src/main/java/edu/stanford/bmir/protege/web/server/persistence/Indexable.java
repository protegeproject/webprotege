package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Oct 2016
 */
public interface Indexable {

    void ensureIndexes(@Nonnull MongoCollection<Document> collection);
}

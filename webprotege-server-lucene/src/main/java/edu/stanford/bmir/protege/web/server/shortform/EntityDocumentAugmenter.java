package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.document.Document;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public interface EntityDocumentAugmenter {

    void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document);
}

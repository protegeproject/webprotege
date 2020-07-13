package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface LuceneIndexUpdater {

    void updateIndexForEntity(@Nonnull OWLEntity entity);
}

package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface LuceneIndexUpdater {

    void updateIndexForEntities(@Nonnull Collection<OWLEntity> entities);
}

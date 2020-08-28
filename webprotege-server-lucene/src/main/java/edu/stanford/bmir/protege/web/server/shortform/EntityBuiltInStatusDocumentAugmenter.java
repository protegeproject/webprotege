package edu.stanford.bmir.protege.web.server.shortform;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.BUILT_IN_FALSE;
import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.BUILT_IN_TRUE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class EntityBuiltInStatusDocumentAugmenter implements EntityDocumentAugmenter {

    @Inject
    public EntityBuiltInStatusDocumentAugmenter() {
    }

    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        // Built-in status.  Not analyzed
        var builtIn = entity.isBuiltIn() ? BUILT_IN_TRUE : BUILT_IN_FALSE;
        document.add(new StringField(EntityDocumentFieldNames.BUILT_IN, builtIn, Field.Store.NO));
    }
}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
import org.apache.lucene.document.Document;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class EntityLocalNameDocumentAugmenter implements EntityDocumentAugmenter {

    @Nonnull
    private final LocalNameExtractor localNameExtractor;

    @Nonnull
    private final DictionaryLanguageFieldWriter fieldWriter;

    @Inject
    public EntityLocalNameDocumentAugmenter(@Nonnull LocalNameExtractor localNameExtractor,
                                            @Nonnull DictionaryLanguageFieldWriter fieldWriter) {
        this.localNameExtractor = checkNotNull(localNameExtractor);
        this.fieldWriter = checkNotNull(fieldWriter);
    }

    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        // Local name (IRI fragment of trailing path element)
        var entityIri = entity.getIRI();
        var localName = localNameExtractor.getLocalName(entityIri);
        if (!localName.isEmpty()) {
            fieldWriter.addFieldForDictionaryLanguage(document, LocalNameDictionaryLanguage.get(), localName);
        }
    }
}

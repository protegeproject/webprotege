package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.shortform.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class LuceneEntityDocumentTranslatorImpl implements LuceneEntityDocumentTranslator {

    @Nonnull
    private final FieldNameTranslator fieldNameTranslator;

    @Nonnull
    private final EntityBuiltInStatusDocumentAugmenter builtInStatusDocumentAugmenter;

    @Nonnull
    private final EntityLocalNameDocumentAugmenter localNameDocumentAugmenter;

    @Nonnull
    private final EntityPrefixedNameDocumentAugmenter prefixedNameDocumentAugmenter;

    @Nonnull
    private final EntityOboIdDocumentAugmenter oboIdDocumentAugmenter;

    @Nonnull
    private final EntityAnnotationAssertionsDocumentAugmenter annotationAssertionsDocumentAugmenter;

    @Nonnull
    private final SearchFiltersDocumentAugmenter searchFiltersDocumentAugmenter;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public LuceneEntityDocumentTranslatorImpl(@Nonnull FieldNameTranslator fieldNameTranslator,
                                              @Nonnull EntityBuiltInStatusDocumentAugmenter builtInStatusDocumentAugmenter,
                                              @Nonnull EntityLocalNameDocumentAugmenter localNameDocumentAugmenter,
                                              @Nonnull EntityPrefixedNameDocumentAugmenter prefixedNameDocumentAugmenter,
                                              @Nonnull EntityOboIdDocumentAugmenter oboIdDocumentAugmenter,
                                              @Nonnull EntityAnnotationAssertionsDocumentAugmenter annotationAssertionsDocumentAugmenter,
                                              @Nonnull SearchFiltersDocumentAugmenter searchFiltersDocumentAugmenter,
                                              @Nonnull OWLDataFactory dataFactory) {
        this.annotationAssertionsDocumentAugmenter = annotationAssertionsDocumentAugmenter;
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
        this.builtInStatusDocumentAugmenter = checkNotNull(builtInStatusDocumentAugmenter);
        this.localNameDocumentAugmenter = checkNotNull(localNameDocumentAugmenter);
        this.prefixedNameDocumentAugmenter = checkNotNull(prefixedNameDocumentAugmenter);
        this.oboIdDocumentAugmenter = checkNotNull(oboIdDocumentAugmenter);
        this.searchFiltersDocumentAugmenter = checkNotNull(searchFiltersDocumentAugmenter);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Nonnull
    public OWLEntity getEntity(@Nonnull Document luceneDocument) {
        var iri = luceneDocument.get(EntityDocumentFieldNames.IRI);
        var entityType = luceneDocument.get(EntityDocumentFieldNames.ENTITY_TYPE);
        var entityIri = org.semanticweb.owlapi.model.IRI.create(iri);
        if (entityType.equals(EntityType.CLASS.getName())) {
            return dataFactory.getOWLClass(entityIri);
        }
        else if (entityType.equals(EntityType.OBJECT_PROPERTY.getName())) {
            return dataFactory.getOWLObjectProperty(entityIri);
        }
        else if (entityType.equals(EntityType.DATA_PROPERTY.getName())) {
            return dataFactory.getOWLDataProperty(entityIri);
        }
        else if (entityType.equals(EntityType.ANNOTATION_PROPERTY.getName())) {
            return dataFactory.getOWLAnnotationProperty(entityIri);
        }
        else if (entityType.equals(EntityType.NAMED_INDIVIDUAL.getName())) {
            return dataFactory.getOWLNamedIndividual(entityIri);
        }
        else if (entityType.equals(EntityType.DATATYPE.getName())) {
            return dataFactory.getOWLDatatype(entityIri);
        }
        else {
            throw new RuntimeException("Unrecognized entity type: " + entityType);
        }
    }

    @Nonnull
    @Override
    public EntityDictionaryLanguageValues getDictionaryLanguageValues(@Nonnull Document document,
                                                                                        @Nonnull List<DictionaryLanguage> dictionaryLanguages) {
        var entity = getEntity(document);
        var valuesBuilder = ImmutableSetMultimap.<DictionaryLanguage, String>builder();
        for (var dictionaryLanguage : dictionaryLanguages) {
            var values = getValues(document, dictionaryLanguage);
            values.forEach(value -> {
                valuesBuilder.put(dictionaryLanguage, value);
            });

        }
        return EntityDictionaryLanguageValues.get(entity, valuesBuilder.build());
    }

    @Nonnull
    private Stream<String> getValues(@Nonnull Document document, @Nonnull DictionaryLanguage dictionaryLanguage) {
        var fieldName = fieldNameTranslator.getNonTokenizedFieldName(dictionaryLanguage);
        return Stream.of(document.getFields(fieldName))
                .map(IndexableField::stringValue);
    }

    @Nonnull
    @Override
    public Document getLuceneDocument(@Nonnull OWLEntity entity) {
        var document = new Document();

        addEntityType(entity, document);

        addEntityIri(entity, document);

        builtInStatusDocumentAugmenter.augmentDocument(entity, document);

        prefixedNameDocumentAugmenter.augmentDocument(entity, document);

        oboIdDocumentAugmenter.augmentDocument(entity, document);

        localNameDocumentAugmenter.augmentDocument(entity, document);

        annotationAssertionsDocumentAugmenter.augmentDocument(entity, document);

        searchFiltersDocumentAugmenter.augmentDocument(entity, document);

        return document;
    }

    private static void addEntityType(OWLEntity entity, Document document) {
        // Entity Type.  Not analyzed
        var entityType = entity.getEntityType().getName();
        document.add(new StringField(ENTITY_TYPE, entityType, Field.Store.YES));
    }

    private static void addEntityIri(OWLEntity entity, Document document) {
        // Entity IRI.  Not analyzed
        var iri = entity.getIRI().toString();
        document.add(new StringField(EntityDocumentFieldNames.IRI, iri, Field.Store.YES));
    }

    @Override
    @Nonnull
    public Query getEntityDocumentQuery(@Nonnull OWLEntity entity) {
        var iriQuery = new TermQuery(new Term(EntityDocumentFieldNames.IRI, entity.getIRI().toString()));
        var typeQuery = getEntityTypeDocumentQuery(entity.getEntityType());
        return new BooleanQuery.Builder().add(iriQuery, BooleanClause.Occur.MUST)
                                         .add(typeQuery, BooleanClause.Occur.MUST)
                                         .build();
    }

    @Nonnull
    public Query getEntityTypeDocumentQuery(@Nonnull EntityType<?> entityType) {
        return new TermQuery(new Term(EntityDocumentFieldNames.ENTITY_TYPE, entityType.toString()));
    }


}

package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public class LuceneEntityDocumentTranslatorImpl implements LuceneEntityDocumentTranslator {

    @Nonnull
    private final ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final DictionaryLanguage2FieldNameTranslator fieldNameTranslator;

    @Nonnull
    private final LocalNameExtractor localNameExtractor = new LocalNameExtractor();

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public LuceneEntityDocumentTranslatorImpl(@Nonnull ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                              @Nonnull DictionaryLanguage2FieldNameTranslator fieldNameTranslator,
                                              @Nonnull OWLDataFactory dataFactory) {
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
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
    public EntityShortForms getEntityShortForms(@Nonnull Document document,
                                                @Nonnull List<DictionaryLanguage> dictionaryLanguages) {
        var entity = getEntity(document);
        var shortForms = ImmutableMap.<DictionaryLanguage, String>builder();
        for(var dictionaryLanguage : dictionaryLanguages) {
            var shortForm = getShortForm(document, dictionaryLanguage);
            if(shortForm != null) {
                shortForms.put(dictionaryLanguage, shortForm);
            }
        }
        return EntityShortForms.get(entity, shortForms.build());
    }

    @Nullable
    private String getShortForm(@Nonnull Document document,
                                @Nonnull DictionaryLanguage dictionaryLanguage) {
        var fieldName = fieldNameTranslator.getOriginalValueFieldName(dictionaryLanguage);
        return document.get(fieldName);
    }

    @Nonnull
    @Override
    public Document getLuceneDocument(@Nonnull OWLEntity entity) {
        var entityType = entity.getEntityType().getName();
        var iri = entity.getIRI().toString();
        var document = new Document();
        document.add(new StringField(EntityDocumentFieldNames.ENTITY_TYPE, entityType, Field.Store.YES));
        document.add(new StringField(EntityDocumentFieldNames.IRI, iri, Field.Store.YES));

        var localName = localNameExtractor.getLocalName(entity.getIRI());
        var localNameFieldNameAnalyzed = fieldNameTranslator.getAnalyzedValueFieldName(DictionaryLanguage.localName());
        document.add(new TextField(localNameFieldNameAnalyzed, localName, Field.Store.YES));
        var localNameFieldOriginal = fieldNameTranslator.getOriginalValueFieldName(DictionaryLanguage.localName());
        document.add(new StringField(localNameFieldOriginal, localName, Field.Store.YES));
        annotationAssertionsIndex.getAnnotationAssertionAxioms(entity.getIRI())
                              .filter(ax -> ax.getValue() instanceof OWLLiteral)
                              .flatMap(this::toFields)
                              .forEach(document::add);
        return document;
    }

    @Override
    @Nonnull
    public Query getEntityDocumentQuery(@Nonnull OWLEntity entity) {
        var iriQuery = new TermQuery(new Term(EntityDocumentFieldNames.IRI, entity.getIRI().toString()));
        var typeQuery = new TermQuery(new Term(EntityDocumentFieldNames.ENTITY_TYPE, entity.getEntityType().toString()));
        return new BooleanQuery.Builder()
                .add(iriQuery, BooleanClause.Occur.MUST)
                .add(typeQuery, BooleanClause.Occur.MUST)
                .build();
    }

    public Stream<Field> toFields(OWLAnnotationAssertionAxiom ax) {
        var literal = (OWLLiteral) ax.getValue();
        var annotationPropertyIri = ax.getProperty().getIRI();
        var dictionaryLanguage = DictionaryLanguage.create(annotationPropertyIri, literal.getLang());
        var lexicalValue = literal.getLiteral();

        var valueFieldName = fieldNameTranslator.getOriginalValueFieldName(dictionaryLanguage);
        var valueField = new StringField(valueFieldName, lexicalValue, Field.Store.YES);

        var wordFieldName = fieldNameTranslator.getAnalyzedValueFieldName(dictionaryLanguage);
        var wordField = new TextField(wordFieldName, lexicalValue, Field.Store.NO);

        return Stream.of(wordField,
                         valueField);
    }
}

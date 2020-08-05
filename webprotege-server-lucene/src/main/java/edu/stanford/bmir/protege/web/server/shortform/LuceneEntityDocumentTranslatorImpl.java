package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.project.BuiltInPrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.obo.OboId;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.shortform.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.*;

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

    @Nonnull
    private final ImmutableMap<String, String> builtInPrefixDeclarationsByPrefix;

    @Inject
    public LuceneEntityDocumentTranslatorImpl(@Nonnull ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                              @Nonnull DictionaryLanguage2FieldNameTranslator fieldNameTranslator,
                                              @Nonnull OWLDataFactory dataFactory,
                                              @Nonnull BuiltInPrefixDeclarations builtInPrefixDeclarations) {
        this.annotationAssertionsIndex = checkNotNull(annotationAssertionsIndex);
        this.fieldNameTranslator = checkNotNull(fieldNameTranslator);
        this.dataFactory = checkNotNull(dataFactory);
        builtInPrefixDeclarationsByPrefix = getPrefixDeclarationsByPrefix(builtInPrefixDeclarations);
    }

    private static ImmutableMap<String, String> getPrefixDeclarationsByPrefix(@Nonnull BuiltInPrefixDeclarations builtInPrefixDeclarations) {
        return builtInPrefixDeclarations.getPrefixDeclarations()
                                        .stream()
                                        .collect(toImmutableMap(PrefixDeclaration::getPrefix,
                                                                PrefixDeclaration::getPrefixName,
                                                                (leftPrefixDecl, rightPrefixDecl) -> rightPrefixDecl));
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
        for (var dictionaryLanguage : dictionaryLanguages) {
            var shortForm = getShortForm(document, dictionaryLanguage);
            if (shortForm != null) {
                shortForms.put(dictionaryLanguage, shortForm);
            }
        }
        return EntityShortForms.get(entity, shortForms.build());
    }

    @Nullable
    private String getShortForm(@Nonnull Document document, @Nonnull DictionaryLanguage dictionaryLanguage) {
        var fieldName = fieldNameTranslator.getOriginalValueFieldName(dictionaryLanguage);
        return document.get(fieldName);
    }

    @Nonnull
    @Override
    public Document getLuceneDocument(@Nonnull OWLEntity entity) {
        var entityType = entity.getEntityType().getName();
        IRI entityIri = entity.getIRI();
        var iri = entityIri.toString();
        var document = new Document();
        // Entity Type
        document.add(new StringField(EntityDocumentFieldNames.ENTITY_TYPE, entityType, Field.Store.YES));
        // Entity IRI
        document.add(new StringField(EntityDocumentFieldNames.IRI, iri, Field.Store.YES));

        var builtIn = entity.isBuiltIn() ? BUILT_IN_TRUE : BUILT_IN_FALSE;
        document.add(new StringField(EntityDocumentFieldNames.BUILT_IN, builtIn, Field.Store.NO));

        var entityIriPrefix = entityIri.getNamespace();
        var prefixName = builtInPrefixDeclarationsByPrefix.get(entityIriPrefix);
        if (prefixName != null) {
            var prefixedName = prefixName + entityIri.getFragment();
            var localNameDictionaryLanguage = PrefixedNameDictionaryLanguage.get();
            addFieldForDictionaryLanguage(document, localNameDictionaryLanguage, prefixedName);
        }


        var oboId = OboId.getOboId(entityIri);
        oboId.ifPresent(s -> addFieldForDictionaryLanguage(document, OboIdDictionaryLanguage.get(), s));

        var localName = localNameExtractor.getLocalName(entityIri);
        addFieldForDictionaryLanguage(document, LocalNameDictionaryLanguage.get(), localName);



        var deprecatedAssertions = new ArrayList<OWLAnnotationAssertionAxiom>();
        annotationAssertionsIndex.getAnnotationAssertionAxioms(entityIri)
                                 .filter(ax -> ax.getValue() instanceof OWLLiteral)
                                 .forEach(ax -> {
                                     toFields(ax).forEach(document::add);
                                     if(ax.isDeprecatedIRIAssertion()) {
                                         deprecatedAssertions.add(ax);
                                     }
                                 });

        // Special treatment for deprecated to allow search filtering

        var deprecated = !deprecatedAssertions.isEmpty();
        var deprecatedFieldValue = deprecated ? DEPRECATED_TRUE : DEPRECATED_FALSE;
        document.add(new StringField(EntityDocumentFieldNames.DEPRECATED, deprecatedFieldValue, Field.Store.NO));


        return document;
    }

    private void addFieldForDictionaryLanguage(Document document, DictionaryLanguage language, String value) {
        var localNameFieldNameAnalyzed = fieldNameTranslator.getAnalyzedValueFieldName(language);
        document.add(new TextField(localNameFieldNameAnalyzed, value, Field.Store.YES));
        var localNameFieldOriginal = fieldNameTranslator.getOriginalValueFieldName(language);
        document.add(new TextField(localNameFieldOriginal, value, Field.Store.YES));
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
        return new TermQuery(new Term(EntityDocumentFieldNames.ENTITY_TYPE,
                                      entityType.toString()));
    }

    private Stream<Field> toFields(OWLAnnotationAssertionAxiom ax) {
        var literal = (OWLLiteral) ax.getValue();
        var annotationPropertyIri = ax.getProperty().getIRI();
        var dictionaryLanguage = AnnotationAssertionDictionaryLanguage.get(annotationPropertyIri, literal.getLang());
        var lexicalValue = literal.getLiteral();


        var valueFieldName = fieldNameTranslator.getOriginalValueFieldName(dictionaryLanguage);
        var valueField = new StringField(valueFieldName, lexicalValue, Field.Store.YES);

        var wordFieldName = fieldNameTranslator.getAnalyzedValueFieldName(dictionaryLanguage);
        var wordField = new TextField(wordFieldName, lexicalValue, Field.Store.NO);

        return Stream.of(wordField, valueField);
    }
}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.AnnotationAssertionPathDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;
import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.DEPRECATED_FALSE;
import static edu.stanford.bmir.protege.web.server.shortform.EntityDocumentFieldNames.DEPRECATED_TRUE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
public class EntityAnnotationAssertionsDocumentAugmenter implements EntityDocumentAugmenter {

    @Nonnull
    private final ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Nonnull
    private final DictionaryLanguageFieldWriter fieldWriter;


    @Inject
    public EntityAnnotationAssertionsDocumentAugmenter(@Nonnull ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex,
                                                       @Nonnull DictionaryLanguageFieldWriter fieldWriter) {
        this.annotationAssertionsIndex = annotationAssertionsIndex;
        this.fieldWriter = fieldWriter;
    }

    @Override
    public void augmentDocument(@Nonnull OWLEntity entity, @Nonnull Document document) {
        addAnnotationAssertions(entity, document);
    }

    private void addAnnotationAssertions(OWLEntity entity, Document document) {
        var entityIri = entity.getIRI();
        var deprecatedAssertions = new ArrayList<OWLAnnotationAssertionAxiom>();
        annotationAssertionsIndex.getAnnotationAssertionAxioms(entityIri)
                                 .forEach(ax -> {
                                     var path = new LinkedHashSet<OWLAnnotationProperty>();
                                     processAnnotationAssertionAxiom(ax, path, document);
                                     if(ax.isDeprecatedIRIAssertion()) {
                                         deprecatedAssertions.add(ax);
                                     }
                                 });

        // Special treatment for deprecated to allow search filtering

        var deprecated = !deprecatedAssertions.isEmpty();
        var deprecatedFieldValue = deprecated ? DEPRECATED_TRUE : DEPRECATED_FALSE;
        document.add(new StringField(EntityDocumentFieldNames.DEPRECATED, deprecatedFieldValue, Field.Store.NO));
    }

    /**
     * Process a path to an annotation value that is a literal.
     * @param ax The axiom
     * @param path The current path
     * @throws ClassCastException if the axiom does not have a subject that is a literal
     */
    private void processAnnotationAssertionAxiom(OWLAnnotationAssertionAxiom ax,
                                                 LinkedHashSet<OWLAnnotationProperty> path,
                                                 Document document) {
        var value = ax.getValue();
        if(value instanceof OWLLiteral) {
            processAnnotationAssertionPathToLiteralTerminal(ax, path, document);
        }
        else if(value instanceof OWLAnnotationSubject) {
            processAnnotationPathToAnnotationSubject(ax, path, document);
        }
    }

    /**
     * Process a path to an annotation subject (either an IRI or anonymous individual)
     * @param ax The annotation assertion axiom
     * @param path The current path
     * @param document The document to add fields to
     */
    private void processAnnotationPathToAnnotationSubject(@Nonnull OWLAnnotationAssertionAxiom ax,
                                                          @Nonnull LinkedHashSet<OWLAnnotationProperty> path,
                                                          @Nonnull Document document) {
        var subject = (OWLAnnotationSubject) ax.getValue();
        if(path.add(ax.getProperty())) {
            annotationAssertionsIndex.getAnnotationAssertionAxioms(subject)
                                     .forEach(nestedAx -> processAnnotationAssertionAxiom(nestedAx, path, document));
            path.remove(ax.getProperty());
        }
    }

    private void processAnnotationAssertionPathToLiteralTerminal(OWLAnnotationAssertionAxiom ax,
                                                                 LinkedHashSet<OWLAnnotationProperty> path,
                                                                 Document document) {
        var literal = (OWLLiteral) ax.getValue();
        // Push
        path.add(ax.getProperty());
        var dictionaryLanguage = getDictionaryLanguage(path, literal.getLang());
        // Remove last element
        path.remove(ax.getProperty());

        var lexicalValue = literal.getLiteral();
        fieldWriter.addFieldForDictionaryLanguage(document, dictionaryLanguage, lexicalValue);
    }

    private static DictionaryLanguage getDictionaryLanguage(LinkedHashSet<OWLAnnotationProperty> path,
                                                            String lang) {
        if(path.size() == 1) {
            var annotationPropertyIri = path.stream().findFirst().orElseThrow().getIRI();
            return AnnotationAssertionDictionaryLanguage.get(annotationPropertyIri, lang);
        }
        else {
            var iriPath = path.stream()
                              .map(OWLAnnotationProperty::getIRI)
                              .collect(toImmutableList());
            return AnnotationAssertionPathDictionaryLanguage.get(iriPath, lang);
        }
    }
}

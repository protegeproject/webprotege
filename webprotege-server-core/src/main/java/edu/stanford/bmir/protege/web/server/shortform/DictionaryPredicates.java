package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryPredicates {

    public static boolean isDictionaryLang(@Nonnull DictionaryCache dictionaryCache,
                                           @Nonnull OWLAnnotationAssertionAxiom ax) {
        return ax.getValue().isLiteral()
                && ((OWLLiteral) ax.getValue()).getLang().equalsIgnoreCase(dictionaryCache.getLang());
    }

    public static boolean isDictionaryProperty(@Nonnull DictionaryCache dictionaryCache,
                                               @Nonnull OWLAnnotationAssertionAxiom ax) {
        return ax.getProperty().equals(dictionaryCache.getAnnotationPropertyIri());
    }

    public static boolean isDictionaryAnnotationAssertion(@Nonnull DictionaryCache dictionaryCache,
                                                          @Nonnull OWLAnnotationAssertionAxiom ax) {
        return ax.getSubject().isIRI()
                && isDictionaryLang(dictionaryCache, ax)
                && isDictionaryProperty(dictionaryCache, ax);
    }

    public static boolean isDictionaryCacheFor(@Nonnull DictionaryCache cache,
                                               @Nonnull IRI annotationPropertyIri,
                                               @Nonnull String lang) {
        return cache.getAnnotationPropertyIri().equals(annotationPropertyIri)
                && cache.getLang().equalsIgnoreCase(lang);
    }
}

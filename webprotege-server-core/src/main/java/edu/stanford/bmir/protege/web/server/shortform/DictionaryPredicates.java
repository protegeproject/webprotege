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


    public static boolean isAxiomForDictionary(@Nonnull OWLAnnotationAssertionAxiom ax,
                                               @Nonnull Dictionary dictionary) {
        if(!(ax.getSubject() instanceof IRI)) {
            return false;
        }
        if(!(ax.getValue() instanceof OWLLiteral)) {
            return false;
        }
        IRI iri = ax.getProperty().getIRI();
        String lang = ((OWLLiteral) ax.getValue()).getLang();
        return isDictionaryFor(dictionary, iri, lang);
    }

    public static boolean isDictionaryFor(@Nonnull Dictionary dictionary,
                                          @Nonnull IRI annotationPropertyIri,
                                          @Nonnull String lang) {
        return dictionary.getLanguage().matches(annotationPropertyIri, lang);
    }
}

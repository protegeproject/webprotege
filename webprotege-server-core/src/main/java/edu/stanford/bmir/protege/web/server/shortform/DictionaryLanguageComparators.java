package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Apr 2018
 */
public class DictionaryLanguageComparators {

    private static final Comparator<DictionaryLanguage> byIriPriority = comparing(DictionaryLanguage::getAnnotationPropertyIri,
                                                                                  nullsLast(comparing(iri -> {
                                                                                      if (isSkosPrefLabel(iri)) {
                                                                                          return 0;
                                                                                      }
                                                                                      else if (isRdfsLabel(iri)) {
                                                                                          return 1;
                                                                                      }
                                                                                      else {
                                                                                          return 2;
                                                                                      }
                                                                                  })));

    private static final Comparator<DictionaryLanguage> byLang = comparing(DictionaryLanguage::getLang,
                                                                           nullsLast(naturalOrder()));


    private static final Comparator<DictionaryLanguage> byIri = comparing(DictionaryLanguage::getAnnotationPropertyIri,
                                                                          nullsLast(naturalOrder()));

    private static final Comparator<DictionaryLanguage> COMPARATOR = byIriPriority.thenComparing(byLang)
                                                                                  .thenComparing(byIri);

    public static Comparator<DictionaryLanguage> byIriPriority() {
        return byIriPriority;
    }

    public static Comparator<DictionaryLanguage> byLang() {
        return byLang;
    }

    public static Comparator<DictionaryLanguage> byIri() {
        return byIri;
    }

    private static boolean isRdfsLabel(IRI iri) {
        return iri.equals(RDFS_LABEL.getIRI());
    }

    private static boolean isSkosPrefLabel(IRI iri) {
        return iri.equals(SKOSVocabulary.PREFLABEL.getIRI());
    }

    public static Comparator<DictionaryLanguage> get() {
        return COMPARATOR;
    }
}

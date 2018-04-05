package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryManager {

    @Nonnull
    private final Dictionary dictionary;

    @Nonnull
    private final BuiltInShortFormCache builtInShortFormCache;

    @Nonnull
    private final LocalNameShortFormCache localNameShortFormCache;

    public DictionaryManager(@Nonnull Dictionary dictionary,
                             @Nonnull BuiltInShortFormCache builtInShortFormCache,
                             @Nonnull LocalNameShortFormCache localNameShortFormCache) {
        this.dictionary = checkNotNull(dictionary);
        this.builtInShortFormCache = checkNotNull(builtInShortFormCache);
        this.localNameShortFormCache = checkNotNull(localNameShortFormCache);
    }

    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return Collections.emptySet();
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity, @Nonnull String prefLang) {
        // Built in short form
        if (entity.isBuiltIn()) {
            final String builtInEntityShortForm = builtInShortFormCache.getShortForm(entity, "");
            if(!builtInEntityShortForm.isEmpty()) {
                return builtInEntityShortForm;
            }
        }
        // Preferred language short form on rdfs:label (may be empty)
        final String prefLangShortForm = dictionary.getShortForm(entity, RDFS_LABEL.getIRI(), prefLang, "");
        if(!prefLangShortForm.isEmpty()) {
            return prefLangShortForm;
        }
        // Fall back to local name
        return localNameShortFormCache.getShortForm(entity);
    }

}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.RDFS_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
@ProjectSingleton
public class DictionaryManager {

    @Nonnull
    private final MultiLingualDictionary dictionary;

    @Nonnull
    private final BuiltInShortFormDictionary builtInShortFormDictionary;

    @Nonnull
    private final LocalNameShortFormCache localNameShortFormCache;

    @Inject
    public DictionaryManager(@Nonnull MultiLingualDictionary dictionary,
                             @Nonnull BuiltInShortFormDictionary builtInShortFormDictionary,
                             @Nonnull LocalNameShortFormCache localNameShortFormCache) {
        this.dictionary = checkNotNull(dictionary);
        this.builtInShortFormDictionary = checkNotNull(builtInShortFormDictionary);
        this.localNameShortFormCache = checkNotNull(localNameShortFormCache);
    }

    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return Collections.emptySet();
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages) {
        // Built in short form
        if (entity.isBuiltIn()) {
            final String builtInEntityShortForm = builtInShortFormDictionary.getShortForm(entity, null);
            if(builtInEntityShortForm != null) {
                return builtInEntityShortForm;
            }
        }
        synchronized (this) {
            return dictionary.getShortForm(entity, languages, "");
        }
    }

}

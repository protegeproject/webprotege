package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.Streams;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

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

    @Inject
    public DictionaryManager(@Nonnull MultiLingualDictionary dictionary,
                             @Nonnull BuiltInShortFormDictionary builtInShortFormDictionary) {
        this.dictionary = checkNotNull(dictionary);
        this.builtInShortFormDictionary = checkNotNull(builtInShortFormDictionary);
    }

    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return dictionary.getEntities(shortForm);
    }

    @Nonnull
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages) {
        final String builtInEntityShortForm = builtInShortFormDictionary.getShortForm(entity, null);
        if (builtInEntityShortForm != null) {
            return builtInEntityShortForm;
        }
        return dictionary.getShortForm(entity, languages, "");
    }

    @Nonnull
    public Collection<String> getShortForms() {
        return dictionary.getShortForms();
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<String> searchStrings,
                                                       @Nonnull List<DictionaryLanguage> languages) {
        return Streams.concat(
                builtInShortFormDictionary.getShortFormsContaining(searchStrings),
                dictionary.getShortFormsContaining(searchStrings, languages)
        );
    }
}

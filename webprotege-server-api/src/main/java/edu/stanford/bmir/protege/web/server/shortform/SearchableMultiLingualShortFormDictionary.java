package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-02
 *
 * Represents an interface for searching multi-lingual short forms
 */
public interface SearchableMultiLingualShortFormDictionary {
    /**
     * Gets short forms containing the specified search strings
     *
     * @param searchStrings The search strings.
     * @param entityTypes   The types of entities to be retrieved.
     * @param languages     The list of languages to consider.
     * @return A stream of short forms.
     */
    @Nonnull
    Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                   @Nonnull Set<EntityType<?>> entityTypes,
                                                   @Nonnull List<DictionaryLanguage> languages);
}

package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 * <p>
 * A dictionary that supports look up for multiple languages
 */
@ProjectSingleton
public interface MultiLingualDictionary extends MultiLingualShortFormDictionary {

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

    /**
     * Gets a stream of entities that exactly match the specified short form.
     *
     * @param shortForm The short form.
     * @param languages A list of languages.  All dictionaries that are for the specified languages will be examined.
     */
    @Nonnull
    Stream<OWLEntity> getEntities(@Nonnull String shortForm,
                                  @Nonnull List<DictionaryLanguage> languages);
}

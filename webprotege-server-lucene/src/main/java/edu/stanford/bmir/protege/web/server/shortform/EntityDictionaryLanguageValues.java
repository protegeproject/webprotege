package edu.stanford.bmir.protege.web.server.shortform;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSetMultimap;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-06
 */
@AutoValue
public abstract class EntityDictionaryLanguageValues {

    public static EntityDictionaryLanguageValues get(@Nonnull OWLEntity entity,
                                                     @Nonnull ImmutableSetMultimap<DictionaryLanguage, String> values) {
        return new AutoValue_EntityDictionaryLanguageValues(entity, values);
    }

    @Nonnull
    public abstract OWLEntity getEntity();

    @Nonnull
    public abstract ImmutableSetMultimap<DictionaryLanguage, String> getValues();

    /**
     * Reduces this map of dictionary language values to a map of entity short forms.  Recalling that an entity shortform
     * map only has one value per dictionary language, only the first
     * value for each dictionary language in this map will be kept in the result.
     * @return The {@link EntityShortForms} for this {@link EntityDictionaryLanguageValues} object.
     */
    @Nonnull
    public EntityShortForms reduceToEntityShortForms() {
        var entity = getEntity();
        var map = getValues().entries()
                   .stream()
                   .collect(toImmutableMap(Map.Entry::getKey,
                                           Map.Entry::getValue,
                                           (firstValue, secondValue) -> firstValue));
        return EntityShortForms.get(entity, map);
    }
}

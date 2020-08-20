package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-17
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class SearchResultMatch {

    public static final String ENTITY = "entity";

    public static final String LANGUAGE = "language";

    private static final String LANGUAGE_RENDERING = "languageRendering";

    public static final String VALUE = "value";

    public static final String POSITIONS = "positions";

    @JsonCreator
    public static SearchResultMatch get(@JsonProperty(ENTITY) @Nonnull EntityNode entity,
                                        @JsonProperty(LANGUAGE) @Nonnull DictionaryLanguage matchedDictionaryLanguage,
                                        @JsonProperty(LANGUAGE_RENDERING) @Nonnull ImmutableMap<DictionaryLanguage, String> languageRendering,
                                        @JsonProperty(VALUE) @Nonnull String matchedString,
                                        @JsonProperty(POSITIONS) @Nonnull ImmutableList<SearchResultMatchPosition> searchResultMatchPositions) {
        return new AutoValue_SearchResultMatch(entity,
                                               matchedDictionaryLanguage,
                                               languageRendering,
                                               matchedString,
                                               searchResultMatchPositions);
    }

    @JsonProperty(ENTITY)
    @Nonnull
    public abstract EntityNode getEntity();

    @JsonProperty(LANGUAGE)
    @Nonnull
    public abstract DictionaryLanguage getLanguage();

    @JsonProperty(LANGUAGE_RENDERING)
    @Nonnull
    public abstract ImmutableMap<DictionaryLanguage, String> getLanguageRendering();

    @JsonProperty(VALUE)
    @Nonnull
    public abstract String getValue();

    @JsonProperty(POSITIONS)
    @Nonnull
    public abstract ImmutableList<SearchResultMatchPosition> getPositions();

}

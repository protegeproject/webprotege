package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-17
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class SearchMatchResult {

    public static final String ENTITY = "entity";

    public static final String LANGUAGE = "language";

    public static final String VALUE = "value";

    public static final String POSITIONS = "positions";

    @JsonCreator
    public static SearchMatchResult get(@JsonProperty(ENTITY) @Nonnull OWLEntityData entity,
                                        @JsonProperty(LANGUAGE) @Nonnull DictionaryLanguage matchedDictionaryLanguage,
                                        @JsonProperty(VALUE) @Nonnull String matchedString,
                                        @JsonProperty(POSITIONS) @Nonnull ImmutableList<SearchMatchPosition> searchMatchPositions) {
        return new AutoValue_SearchMatchResult(entity, matchedDictionaryLanguage, matchedString, searchMatchPositions);
    }

    @JsonProperty(ENTITY)
    @Nonnull
    public abstract OWLEntityData getEntity();

    @JsonProperty(LANGUAGE)
    @Nonnull
    public abstract DictionaryLanguage getLanguage();

    @JsonProperty(VALUE)
    @Nonnull
    public abstract String getValue();

    @JsonProperty(POSITIONS)
    @Nonnull
    public abstract ImmutableList<SearchMatchPosition> getPositions();

}

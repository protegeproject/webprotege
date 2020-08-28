package edu.stanford.bmir.protege.web.shared.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class EntitySearchResult {

    public static final String ENTITY = "entity";

    public static final String MATCHES = "matches";

    @Nonnull
    public static EntitySearchResult get(@JsonProperty(ENTITY) @Nonnull EntityNode entity,
                                         @JsonProperty(MATCHES) @Nonnull ImmutableList<SearchResultMatch> matches) {
        return new AutoValue_EntitySearchResult(entity, matches);
    }


    @JsonProperty(ENTITY)
    @Nonnull
    public abstract EntityNode getEntity();

    /**
     * Get the matches for this particular entity
     * @return The list of matches
     */
    @JsonProperty(MATCHES)
    @Nonnull
    public abstract ImmutableList<SearchResultMatch> getMatches();
}

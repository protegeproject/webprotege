package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class TagData implements IsSerializable {

    public static TagData get(@Nonnull String label,
                              @Nonnull String description,
                              @Nonnull Color color,
                              @Nonnull Color backgroundColor,
                              @Nonnull ImmutableList<RootCriteria> criteria,
                              int usageCount) {
        return get(null, label, description, color, backgroundColor, criteria, usageCount);
    }

    @JsonCreator
    public static TagData get(@Nullable TagId tagId,
                              @Nonnull String label,
                              @Nonnull String description,
                              @Nonnull Color color,
                              @Nonnull Color backgroundColor,
                              @Nonnull ImmutableList<RootCriteria> criteria,
                              int usageCount) {
        return new AutoValue_TagData(tagId, label, description, color, backgroundColor, criteria, usageCount);
    }

    @Nonnull
    public Optional<TagId> getTagId() {
        return Optional.ofNullable(_getTagId());
    }

    @Nullable
    protected abstract TagId _getTagId();

    @Nonnull
    public abstract String getLabel();

    @Nonnull
    public abstract String getDescription();

    @Nonnull
    public abstract Color getColor();

    @Nonnull
    public abstract Color getBackgroundColor();

    @Nonnull
    public abstract ImmutableList<RootCriteria> getCriteria();

    public abstract int getUsageCount();

    public TagData withCriteria(ImmutableList<RootCriteria> criteria) {
        return get(_getTagId(),
                   getLabel(),
                   getDescription(),
                   getColor(),
                   getBackgroundColor(),
                   criteria,
                   getUsageCount());
    }


}

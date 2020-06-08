package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormRegionOrdering {

    public static final String REGION_ID = "regionId";

    public static final String DIRECTION = "direction";

    @JsonCreator
    @Nonnull
    public static FormRegionOrdering get(@JsonProperty(REGION_ID) @Nonnull FormRegionId formRegionId,
                                         @JsonProperty(DIRECTION) @Nonnull FormRegionOrderingDirection direction) {
        return new AutoValue_FormRegionOrdering(formRegionId, direction);
    }

    @JsonProperty(REGION_ID)
    @Nonnull
    public abstract FormRegionId getRegionId();

    @JsonProperty(DIRECTION)
    @Nonnull
    public abstract FormRegionOrderingDirection getDirection();

    @JsonIgnore
    public boolean isAscending() {
        return getDirection().equals(FormRegionOrderingDirection.ASC);
    }
}

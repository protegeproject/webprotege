package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FilterName implements IsSerializable {

    @Nonnull
    @JsonCreator
    public static FilterName get(@Nonnull String name) {
        return new AutoValue_FilterName(name);
    }

    @JsonValue
    @Nonnull
    public abstract String getName();
}

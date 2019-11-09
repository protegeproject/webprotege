package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class ChoiceDescriptor implements IsSerializable {

    @JsonCreator
    public static ChoiceDescriptor choice(@Nonnull @JsonProperty("label") LanguageMap label,
                                          @Nonnull @JsonProperty("value") FormDataValue value) {
        return new AutoValue_ChoiceDescriptor(label, value);
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @JsonUnwrapped
    @Nonnull
    public abstract FormDataValue getValue();
}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(DynamicChoiceListSourceDescriptor.TYPE)
public abstract class DynamicChoiceListSourceDescriptor implements ChoiceListSourceDescriptor {

    public static final String TYPE = "Dynamic";

    private static final String CRITERIA = "criteria";

    @JsonCreator
    public static DynamicChoiceListSourceDescriptor get(@JsonProperty(CRITERIA) @Nonnull EntityMatchCriteria criteria) {
        return new AutoValue_DynamicChoiceListSourceDescriptor(criteria);
    }

    @JsonProperty(CRITERIA)
    @Nonnull
    public abstract EntityMatchCriteria getCriteria();

}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;

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
    public static DynamicChoiceListSourceDescriptor get(@JsonProperty(CRITERIA) @Nonnull RootCriteria criteria) {
        if (criteria instanceof CompositeRootCriteria) {
            return new AutoValue_DynamicChoiceListSourceDescriptor((CompositeRootCriteria) criteria);
        }
        else {
            CompositeRootCriteria wrapped = CompositeRootCriteria.get(
                    ImmutableList.of(criteria),
                    MultiMatchType.ALL
            );
            return new AutoValue_DynamicChoiceListSourceDescriptor(wrapped);
        }
    }

    @JsonProperty(CRITERIA)
    @Nonnull
    public abstract CompositeRootCriteria getCriteria();

}

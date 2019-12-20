package edu.stanford.bmir.protege.web.shared.viz;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-06
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName("AnyEdgeType")
public class AnyEdgeTypeCriteria {

    public static AnyEdgeTypeCriteria get() {
        return new AutoValue_AnyEdgeTypeCriteria();
    }
}

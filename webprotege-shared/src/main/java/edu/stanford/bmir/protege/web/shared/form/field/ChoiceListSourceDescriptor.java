package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = FixedChoiceListSourceDescriptor.class, name = FixedChoiceListSourceDescriptor.TYPE),
                      @JsonSubTypes.Type(value = DynamicChoiceListSourceDescriptor.class, name = DynamicChoiceListSourceDescriptor.TYPE)
              })
public interface ChoiceListSourceDescriptor {

}

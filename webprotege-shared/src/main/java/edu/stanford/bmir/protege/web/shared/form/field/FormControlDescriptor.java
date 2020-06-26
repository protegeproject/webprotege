package edu.stanford.bmir.protege.web.shared.form.field;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
                      @JsonSubTypes.Type(value = TextControlDescriptor.class, name = TextControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = NumberControlDescriptor.class, name = NumberControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = SingleChoiceControlDescriptor.class, name = SingleChoiceControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = MultiChoiceControlDescriptor.class, name = MultiChoiceControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = EntityNameControlDescriptor.class, name = EntityNameControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = ImageControlDescriptor.class, name = ImageControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = GridControlDescriptor.class, name = GridControlDescriptor.TYPE),
                      @JsonSubTypes.Type(value = SubFormControlDescriptor.class, name = SubFormControlDescriptor.TYPE)
              })
public interface FormControlDescriptor extends IsSerializable {

    @JsonIgnore
    @Nonnull
    String getAssociatedType();

    <R> R accept(@Nonnull FormControlDescriptorVisitor<R> visitor);
}

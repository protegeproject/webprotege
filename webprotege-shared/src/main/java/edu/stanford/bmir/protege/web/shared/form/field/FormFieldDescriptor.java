package edu.stanford.bmir.protege.web.shared.form.field;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import javax.annotation.Nonnull;

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
                      @JsonSubTypes.Type(value = TextFieldDescriptor.class, name = TextFieldDescriptor.TYPE),
                      @JsonSubTypes.Type(value = ChoiceFieldDescriptor.class, name = ChoiceFieldDescriptor.TYPE),
                      @JsonSubTypes.Type(value = EntityNameFieldDescriptor.class, name = EntityNameFieldDescriptor.TYPE),
                      @JsonSubTypes.Type(value = IndividualNameFieldDescriptor.class,
                                         name = IndividualNameFieldDescriptor.TYPE),
                      @JsonSubTypes.Type(value = ImageFieldDescriptor.class, name = ImageFieldDescriptor.TYPE),
                      @JsonSubTypes.Type(value = NumberFieldDescriptor.class, name = NumberFieldDescriptor.TYPE),
                      @JsonSubTypes.Type(value = SubFormFieldDescriptor.class, name = SubFormFieldDescriptor.TYPE)
              })
public interface FormFieldDescriptor extends IsSerializable {

    @JsonIgnore
    @Nonnull
    String getAssociatedType();
}

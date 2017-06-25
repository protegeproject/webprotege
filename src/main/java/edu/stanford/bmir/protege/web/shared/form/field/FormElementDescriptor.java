package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.HasFormElementId;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementDescriptor implements HasFormElementId, HasRepeatability, Serializable {

    private FormElementId id;

    private String label;

    private FormFieldDescriptor fieldDescriptor;

    private Repeatability repeatability;

    private Required required;

    @GwtSerializationConstructor
    private FormElementDescriptor() {
    }

    public FormElementDescriptor(@Nonnull FormElementId id,
                                 @Nonnull String formLabel,
                                 @Nonnull FormFieldDescriptor fieldDescriptor,
                                 @Nonnull Repeatability repeatability,
                                 @Nonnull Required required) {
        this.id = checkNotNull(id);
        this.label = checkNotNull(formLabel);
        this.repeatability = checkNotNull(repeatability);
        this.fieldDescriptor = checkNotNull(fieldDescriptor);
        this.required = checkNotNull(required);
    }

    @Override
    @JsonUnwrapped
    public FormElementId getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public Repeatability getRepeatability() {
        return repeatability;
    }

    public Required getRequired() {
        return required;
    }

    public FormFieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }
}

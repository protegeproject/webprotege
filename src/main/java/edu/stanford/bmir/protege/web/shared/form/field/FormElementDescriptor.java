package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.Objects;
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

    private String help;

    @GwtSerializationConstructor
    private FormElementDescriptor() {
    }

    public FormElementDescriptor(@Nonnull FormElementId id,
                                 @Nonnull String formLabel,
                                 @Nonnull FormFieldDescriptor fieldDescriptor,
                                 @Nonnull Repeatability repeatability,
                                 @Nonnull Required required,
                                 @Nonnull String help) {
        this.id = checkNotNull(id);
        this.label = checkNotNull(formLabel);
        this.repeatability = checkNotNull(repeatability);
        this.fieldDescriptor = checkNotNull(fieldDescriptor);
        this.required = checkNotNull(required);
        this.help = checkNotNull(help);
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

    public String getHelp() {
        return help;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, label, repeatability, required, fieldDescriptor, help);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormElementDescriptor)) {
            return false;
        }
        FormElementDescriptor other = (FormElementDescriptor) obj;
        return this.id.equals(other.id)
                && this.label.equals(other.label)
                && this.repeatability.equals(other.repeatability)
                && this.required.equals(other.required)
                && this.fieldDescriptor.equals(other.fieldDescriptor)
                && this.help.equals(other.help);
    }
}

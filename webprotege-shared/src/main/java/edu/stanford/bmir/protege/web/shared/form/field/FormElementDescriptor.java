package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.form.HasFormElementId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormElementDescriptor implements HasFormElementId, HasRepeatability, Serializable {

    private FormElementId id;

    private LanguageMap label = LanguageMap.empty();

    private FormFieldDescriptor fieldDescriptor;

    private Repeatability repeatability = Repeatability.NON_REPEATABLE;

    private Optionality optionality = Optionality.REQUIRED;

    private LanguageMap help = LanguageMap.empty();

    @GwtSerializationConstructor
    private FormElementDescriptor() {
    }

    public FormElementDescriptor(@Nonnull FormElementId id,
                                 @Nonnull LanguageMap formLabel,
                                 @Nonnull FormFieldDescriptor fieldDescriptor,
                                 @Nonnull Repeatability repeatability,
                                 @Nonnull Optionality optionality,
                                 @Nonnull LanguageMap help) {
        this.id = checkNotNull(id);
        this.label = checkNotNull(formLabel);
        this.fieldDescriptor = checkNotNull(fieldDescriptor);
        this.repeatability = checkNotNull(repeatability);
        this.optionality = checkNotNull(optionality);
        this.help = checkNotNull(help);
    }

    @Override
    @JsonUnwrapped
    public FormElementId getId() {
        return id;
    }

    public LanguageMap getLabel() {
        return label;
    }

    @Override
    public Repeatability getRepeatability() {
        return repeatability;
    }

    public Optionality getOptionality() {
        return optionality;
    }

    public FormFieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }

    public boolean isNonComposite() {
        return !(fieldDescriptor instanceof CompositeFieldDescriptor);
    }

    public boolean isComposite() {
        return fieldDescriptor instanceof CompositeFieldDescriptor;
    }

    public LanguageMap getHelp() {
        return help;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, label, repeatability, optionality, fieldDescriptor, help);
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
                && Objects.equal(this.label, other.label)
                && this.repeatability.equals(other.repeatability)
                && this.optionality.equals(other.optionality)
                && this.fieldDescriptor.equals(other.fieldDescriptor)
                && Objects.equal(this.help, other.help);
    }


    @Override
    public String toString() {
        return toStringHelper("FormElementDescriptor")
                .addValue(id)
                .add("label", label)
                .add("repeatable", repeatability)
                .add("optionality", optionality)
                .add("help", help)
                .add("descriptor", fieldDescriptor)
                .toString();
    }
}

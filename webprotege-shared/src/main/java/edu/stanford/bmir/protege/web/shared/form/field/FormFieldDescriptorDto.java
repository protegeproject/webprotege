package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.HasFormFieldId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.form.field.FormFieldDescriptor.INITIAL_EXPANSIONS_STATE;

@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormFieldDescriptorDto implements IsSerializable, HasFormFieldId {

    @Nonnull
    public static FormFieldDescriptorDto get(FormFieldId formFieldId,
                                                OwlBinding owlBinding,
                                                LanguageMap newlabel,
                                                FieldRun fieldRun,
                                                FormControlDescriptorDto descriptorDto,
                                                Optionality optionality,
                                                Repeatability repeatability,
                                                boolean newReadOnly,
                                                ExpansionState initialExpansionState,
                                                LanguageMap help) {
        return new AutoValue_FormFieldDescriptorDto(formFieldId,
                                                    owlBinding,
                                                    newlabel,
                                                    fieldRun,
                                                    descriptorDto,
                                                    optionality,
                                                    repeatability,
                                                    newReadOnly,
                                                    initialExpansionState,
                                                    help);
    }


    @Nonnull
    @Override
    @JsonProperty("id")
    public abstract FormFieldId getId();

    @JsonIgnore
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract FieldRun getFieldRun();

    @Nonnull
    public abstract FormControlDescriptorDto getFormControlDescriptor();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    public abstract boolean isReadOnly();

    @Nonnull
    public abstract ExpansionState getInitialExpansionState();

    @Nonnull
    public abstract LanguageMap getHelp();

    @JsonIgnore
    public boolean isComposite() {
        return getFormControlDescriptor() instanceof SubFormControlDescriptor;
    }

    public FormFieldDescriptor toFormFieldDescriptor() {
        return FormFieldDescriptor.get(
                getId(),
                getOwlBindingInternal(),
                getLabel(),
                getFieldRun(),
                getFormControlDescriptor().toFormControlDescriptor(),
                getRepeatability(),
                getOptionality(),
                isReadOnly(),
                getInitialExpansionState(),
                getHelp()
        );
    }
}

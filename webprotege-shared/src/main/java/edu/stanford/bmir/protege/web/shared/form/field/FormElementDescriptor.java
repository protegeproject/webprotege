package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.HasFormElementId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonPropertyOrder({"id", "owlProperty", "label", "elementRun", "fieldDescriptor", "repeatability", "optionality", "help"})
@GwtCompatible(serializable = true)
@AutoValue
public abstract class FormElementDescriptor implements HasFormElementId, HasRepeatability, Serializable {


    @JsonCreator
    @Nonnull
    public static FormElementDescriptor get(@JsonProperty("id") @Nonnull FormElementId id,
                                            @JsonProperty("owlBinding") @Nullable OwlBinding owlBinding,
                                            @JsonProperty("label") @Nullable LanguageMap formLabel,
                                            @JsonProperty("elementRun") @Nullable ElementRun elementRun,
                                            @JsonProperty("fieldDescriptor") @Nonnull FormFieldDescriptor fieldDescriptor,
                                            @JsonProperty("repeatability") @Nullable Repeatability repeatability,
                                            @JsonProperty("optionality") @Nullable Optionality optionality,
                                            @JsonProperty("help") @Nullable LanguageMap help,
                                            @JsonProperty("style") @Nullable Map<String, String> style) {
        return new AutoValue_FormElementDescriptor(id,
                                                   owlBinding,
                                                   formLabel == null ? LanguageMap.empty() : formLabel,
                                                   elementRun == null ? ElementRun.START : elementRun,
                                                   fieldDescriptor,
                                                   optionality == null ? Optionality.REQUIRED : optionality,
                                                   repeatability == null ? Repeatability.NON_REPEATABLE : repeatability,
                                                   help == null ? LanguageMap.empty() : help,
                                                   style == null ? ImmutableMap.of() : style);
    }

    public static FormElementDescriptor getDefault() {
        return FormElementDescriptor.get(
                FormElementId.get(""),
                null,
                LanguageMap.empty(),
                ElementRun.START,
                new TextFieldDescriptor(LanguageMap.empty(),
                                        StringType.SIMPLE_STRING,
                                        LineMode.SINGLE_LINE,
                                        "",
                                        LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                LanguageMap.empty(),
                Collections.emptyMap()
        );
    }

    @Nonnull
    @Override
    @JsonProperty("id")
    public abstract FormElementId getId();

    @JsonIgnore
    @Nullable
    protected abstract OwlBinding getOwlBindingInternal();

    @Nonnull
    public Optional<OwlBinding> getOwlBinding() {
        return Optional.ofNullable(getOwlBindingInternal());
    }

    @JsonIgnore
    public Optional<OWLProperty> getOwlProperty() {
        return getOwlBinding().flatMap(OwlBinding::getOwlProperty);
    }

    @Nonnull
    public abstract LanguageMap getLabel();

    @Nonnull
    public abstract ElementRun getElementRun();

    @Nonnull
    public abstract FormFieldDescriptor getFieldDescriptor();

    @Nonnull
    public abstract Optionality getOptionality();

    @Nonnull
    public abstract Repeatability getRepeatability();

    @Nonnull
    public abstract LanguageMap getHelp();

    @Nonnull
    public abstract Map<String, String> getStyle();
    
    @JsonIgnore
    public boolean isComposite() {
        return getFieldDescriptor() instanceof SubFormFieldDescriptor;
    }

    @JsonIgnore
    public boolean isNonComposite() {
        return !(getFieldDescriptor() instanceof SubFormFieldDescriptor);
    }
}

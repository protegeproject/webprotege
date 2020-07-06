package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.NumberControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class NumberControlDataDto implements FormControlDataDto, Comparable<NumberControlDataDto> {

    private Double numericValue;

    @Nonnull
    public static NumberControlDataDto get(@Nonnull NumberControlDescriptor descriptor,
                                           @Nonnull OWLLiteral value,
                                           int depth) {
        return new AutoValue_NumberControlDataDto(depth, descriptor, value);
    }

    @Nonnull
    public abstract NumberControlDescriptor getDescriptor();

    @JsonProperty("value")
    @Nullable
    protected abstract OWLLiteral getValueInternal();

    @Nonnull
    @JsonIgnore
    public Optional<OWLLiteral> getValue() {
        return Optional.ofNullable(getValueInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public NumberControlData toFormControlData() {
        return NumberControlData.get(getDescriptor(),
                                     getValueInternal());
    }

    private Double getNumericValue() {
        if (numericValue == null) {
            OWLLiteral literal = getValueInternal();
            if (literal == null) {
                numericValue = Double.MIN_VALUE;
            } else {
                try {
                    numericValue = Double.parseDouble(literal.getLiteral().trim());
                } catch (NumberFormatException e) {
                    numericValue = Double.MIN_VALUE;
                }
            }
        }
        return numericValue;
    }

    @Override
    public int compareTo(@Nonnull NumberControlDataDto o) {
        return getNumericValue().compareTo(o.getNumericValue());
    }
}

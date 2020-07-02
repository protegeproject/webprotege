package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class TextControlDataDto implements FormControlDataDto, Comparable<TextControlDataDto> {

    private static Comparator<OWLLiteral> literalComparator = Comparator.nullsLast(Comparator.comparing(OWLLiteral::getLang)
            .thenComparing(OWLLiteral::getLiteral, String::compareToIgnoreCase)
            .thenComparing(OWLLiteral::getDatatype));

    @Nonnull
    public static TextControlDataDto get(@Nonnull TextControlDescriptor descriptor,
                                         @Nonnull OWLLiteral value,
                                         int depth) {
        return new AutoValue_TextControlDataDto(depth, descriptor, value);
    }

    @JsonProperty("descriptor")
    @Nonnull
    public abstract TextControlDescriptor getDescriptor();

    @JsonProperty("value")
    @Nullable
    protected abstract OWLLiteral getValueInternal();

    @JsonIgnore
    @Nonnull
    public Optional<OWLLiteral> getValue() {
        return Optional.ofNullable(getValueInternal());
    }

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public TextControlData toFormControlData() {
        return TextControlData.get(getDescriptor(),
                getValueInternal());
    }

    @Override
    public int compareTo(@Nonnull TextControlDataDto o) {
        return literalComparator.compare(this.getValueInternal(), o.getValueInternal());
    }
}

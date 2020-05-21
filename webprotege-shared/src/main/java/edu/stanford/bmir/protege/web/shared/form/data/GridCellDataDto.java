package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Comparators;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellDataDto {

    public static final int SAME = 0;

    public static final int BEFORE = -1;

    public static final int AFTER = 1;

    public static final Comparator<Iterable<PrimitiveFormControlDataDto>> LEXICOGRAPHICAL_PRIMITIVE_FORM_CONTROL_DATA_COMPARATOR = Comparators.lexicographical(
            PrimitiveFormControlDataDto::compareTo);

    public static final Comparator<Optional<PrimitiveFormControlDataDto>> OPTIONAL_PRIMITIVE_FORM_CONTROL_DATA_DTO_COMPARATOR = Comparators.emptiesFirst(
            PrimitiveFormControlDataDto::compareTo);

    public static final Comparator<Optional<OWLEntityData>> OPTIONAL_ENTITY_DATA_COMPARATOR = Comparators.emptiesFirst(
            OWLEntityData::compareTo);

    public static final Comparator<Optional<OWLLiteral>> OPTIONAL_LITERAL_COMPARATOR = Comparators.emptiesFirst(
                Comparator.comparing((OWLLiteral l) -> l.getLiteral().toLowerCase())
                .thenComparing((OWLLiteral l) -> l.getLang().toLowerCase()).thenComparing(OWLLiteral::getDatatype)
            );

    public static GridCellDataDto get(@Nonnull GridColumnId columnId,
                                      @Nullable Page<FormControlDataDto> values) {
        return new AutoValue_GridCellDataDto(columnId, values);
    }

    public int compareTo(GridCellDataDto otherCellData) {
        Page<FormControlDataDto> valuesPage = getValues();
        Page<FormControlDataDto> otherValuesPage = otherCellData.getValues();

        // Empty values come first
        if(valuesPage.getPageSize() == 0) {
            if(otherValuesPage.getPageSize() == 0) {
                return SAME;
            }
            else {
                return BEFORE;
            }
        }
        else {
            if(otherValuesPage.getPageSize() == 0) {
                return AFTER;
            }
        }

        List<FormControlDataDto> values = valuesPage.getPageElements();
        List<FormControlDataDto> otherValues = otherValuesPage.getPageElements();
        for(int i = 0; i < values.size() && i < otherValues.size(); i++) {
            FormControlDataDto formControlData = values.get(i);
            FormControlDataDto otherControlData = otherValues.get(i);
            if(formControlData instanceof TextControlDataDto && otherControlData instanceof TextControlDataDto) {
                Optional<OWLLiteral> value = ((TextControlDataDto) formControlData).getValue();
                Optional<OWLLiteral> otherValue = ((TextControlDataDto) otherControlData).getValue();
                return OPTIONAL_LITERAL_COMPARATOR.compare(value, otherValue);
            }
            if(formControlData instanceof EntityNameControlDataDto && otherControlData instanceof EntityNameControlDataDto) {
                Optional<OWLEntityData> value = ((EntityNameControlDataDto) formControlData).getEntity();
                Optional<OWLEntityData> otherValue = ((EntityNameControlDataDto) otherControlData).getEntity();
                return OPTIONAL_ENTITY_DATA_COMPARATOR.compare(value, otherValue);
            }
            if(formControlData instanceof SingleChoiceControlDataDto && otherControlData instanceof SingleChoiceControlDataDto) {
                Optional<PrimitiveFormControlDataDto> value = ((SingleChoiceControlDataDto) formControlData).getChoice();
                Optional<PrimitiveFormControlDataDto> otherValue = ((SingleChoiceControlDataDto) otherControlData).getChoice();
                return OPTIONAL_PRIMITIVE_FORM_CONTROL_DATA_DTO_COMPARATOR.compare(value, otherValue);
            }
            if(formControlData instanceof MultiChoiceControlDataDto && otherControlData instanceof MultiChoiceControlDataDto) {
                ImmutableList<PrimitiveFormControlDataDto> value = ((MultiChoiceControlDataDto) formControlData).getValues();
                ImmutableList<PrimitiveFormControlDataDto> otherValue = ((MultiChoiceControlDataDto) otherControlData).getValues();
                return LEXICOGRAPHICAL_PRIMITIVE_FORM_CONTROL_DATA_COMPARATOR.compare(value, otherValue);
            }
        }

        return 0;
    }

    @Nonnull
    public abstract GridColumnId getColumnId();

    @Nonnull
    public abstract Page<FormControlDataDto> getValues();

    public GridCellData toGridCellData() {
        return GridCellData.get(getColumnId(),
                getValues().transform(FormControlDataDto::toFormControlData));
    }
}

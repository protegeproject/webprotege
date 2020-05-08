package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellDataDto {

    public static GridCellData get(@Nonnull GridColumnId columnId,
                                   @Nullable ImmutableList<FormControlData> values) {
        return new AutoValue_GridCellData(columnId, values);
    }

    public int compareTo(GridCellDataDto otherCellData) {
        ImmutableList<FormControlDataDto> values = getValues();
        ImmutableList<FormControlDataDto> otherValues = otherCellData.getValues();
        for(int i = 0; i < values.size() && i < otherValues.size(); i++) {
            FormControlDataDto formControlData = values.get(i);
            FormControlDataDto otherControlData = otherValues.get(i);
            if(formControlData instanceof TextControlDataDto && otherControlData instanceof TextControlDataDto) {
                Optional<OWLLiteral> value = ((TextControlDataDto) formControlData).getValue();
                Optional<OWLLiteral> otherValue = ((TextControlDataDto) otherControlData).getValue();
                if(value.isPresent() && otherValue.isPresent()) {
                    int diff = value.get().compareTo(otherValue.get());
                    if(diff != 0) {
                        return diff;
                    }
                }
            }
            if(formControlData instanceof EntityNameControlDataDto && otherControlData instanceof EntityNameControlDataDto) {
                Optional<OWLEntityData> value = ((EntityNameControlDataDto) formControlData).getEntity();
                Optional<OWLEntityData> otherValue = ((EntityNameControlDataDto) otherControlData).getEntity();
                if(value.isPresent() && otherValue.isPresent()) {
                    int diff = value.get().compareTo(otherValue.get());
                    if(diff != 0) {
                        return diff;
                    }
                }
            }
        }

        return 0;
    }

    @Nonnull
    public abstract GridColumnId getColumnId();

    @Nonnull
    public abstract ImmutableList<FormControlDataDto> getValues();
}

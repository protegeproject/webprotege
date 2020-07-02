package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellData {

    public static GridCellData get(@Nonnull GridColumnId columnId,
                                   @Nullable Page<FormControlData> values) {
        return new AutoValue_GridCellData(columnId, values);
    }

    public int compareTo(GridCellData otherCellData) {
        Page<FormControlData> valuesPage = getValues();
        Page<FormControlData> otherValuesPage = otherCellData.getValues();
        List<FormControlData> values = valuesPage.getPageElements();
        List<FormControlData> otherValues = otherValuesPage.getPageElements();
        for(int i = 0; i < values.size() && i < otherValues.size(); i++) {
            FormControlData formControlData = values.get(i);
            FormControlData otherControlData = otherValues.get(i);
            if(formControlData instanceof TextControlData && otherControlData instanceof TextControlData) {
                Optional<OWLLiteral> value = ((TextControlData) formControlData).getValue();
                Optional<OWLLiteral> otherValue = ((TextControlData) otherControlData).getValue();
                if(value.isPresent() && otherValue.isPresent()) {
                    int diff = value.get().compareTo(otherValue.get());
                    if(diff != 0) {
                        return diff;
                    }
                }
            }
            if(formControlData instanceof EntityNameControlData && otherControlData instanceof EntityNameControlData) {
                Optional<OWLEntity> value = ((EntityNameControlData) formControlData).getEntity();
                Optional<OWLEntity> otherValue = ((EntityNameControlData) otherControlData).getEntity();
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
    public abstract Page<FormControlData> getValues();
}

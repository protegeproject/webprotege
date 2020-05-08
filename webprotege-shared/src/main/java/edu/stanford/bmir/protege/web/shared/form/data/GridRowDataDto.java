package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
@GwtCompatible(serializable = true)
public  abstract class GridRowDataDto implements FormControlDataDto, Comparable<GridRowDataDto> {

    @Nonnull
    public static GridRowDataDto get(@Nullable FormSubjectDto subject,
                                     @Nonnull ImmutableList<GridCellDataDto> cellData) {
        return new AutoValue_GridRowDataDto(subject, cellData);
    }


    @Nullable
    protected abstract FormSubjectDto getSubjectInternal();

    @Nonnull
    public Optional<FormSubjectDto> getSubject() {
        return Optional.ofNullable(getSubjectInternal());
    }

    @Nonnull
    public abstract ImmutableList<GridCellDataDto> getCells();

    @Override
    public int compareTo(@Nonnull GridRowDataDto o) {
        ImmutableList<GridCellDataDto> cells = getCells();
        ImmutableList<GridCellDataDto> otherCells = o.getCells();
        for(int i = 0; i < cells.size() && i < otherCells.size(); i++) {
            GridCellDataDto cellData = cells.get(i);
            GridCellDataDto otherCellData = otherCells.get(i);
            int diff = cellData.compareTo(otherCellData);
            if(diff != 0) {
                return diff;
            }
        }
        return 0;
    }
}

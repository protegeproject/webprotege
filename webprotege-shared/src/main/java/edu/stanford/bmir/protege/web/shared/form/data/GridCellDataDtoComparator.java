package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.Comparators;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

public class GridCellDataDtoComparator implements Comparator<GridCellDataDto> {

    @Nonnull
    private Comparator<Iterable<FormControlDataDto>> lexComparator;

    @Inject
    public GridCellDataDtoComparator(@Nonnull FormControlDataDtoComparator formControlDataDtoComparator) {
        this.lexComparator = Comparators.lexicographical(formControlDataDtoComparator);
    }

    @Override
    public int compare(GridCellDataDto cellData1, GridCellDataDto cellData2) {
        List<FormControlDataDto> valuesPage = cellData1.getValues()
                                                       .getPageElements();
        List<FormControlDataDto> otherValuesPage = cellData2.getValues()
                                                            .getPageElements();
        return lexComparator.compare(valuesPage, otherValuesPage);
    }

}

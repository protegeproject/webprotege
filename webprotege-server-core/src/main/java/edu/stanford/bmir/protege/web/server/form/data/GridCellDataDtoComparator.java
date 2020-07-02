package edu.stanford.bmir.protege.web.server.form.data;

import com.google.common.collect.Comparators;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.GridCellDataDto;

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
        var valuesPage = cellData1.getValues().getPageElements();
        var otherValuesPage = cellData2.getValues().getPageElements();
        return lexComparator.compare(valuesPage, otherValuesPage);
    }

}

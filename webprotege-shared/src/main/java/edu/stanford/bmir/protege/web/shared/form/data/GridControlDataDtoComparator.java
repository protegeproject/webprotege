package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.Comparators;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

public class GridControlDataDtoComparator implements Comparator<GridControlDataDto> {

    private Comparator<Iterable<GridRowDataDto>> lexicographical;

    @Inject
    public GridControlDataDtoComparator(@Nonnull GridRowDtoByColumnIndexComparator gridRowDtoByColumnIndexComparator) {
        this.lexicographical = Comparators.lexicographical(gridRowDtoByColumnIndexComparator);
    }

    @Override
    public int compare(GridControlDataDto o1, GridControlDataDto o2) {
        return lexicographical.compare(o1.getRows(), o2.getRows());
    }
}

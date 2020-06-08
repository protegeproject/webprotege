package edu.stanford.bmir.protege.web.server.form.data;

import com.google.common.collect.Comparators;
import edu.stanford.bmir.protege.web.shared.form.data.GridControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowDataDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridControlDataDtoComparator implements Comparator<GridControlDataDto> {

    @Nonnull
    private final GridRowDataDtoComparatorFactory comparatorFactory;

    @Inject
    public GridControlDataDtoComparator(@Nonnull GridRowDataDtoComparatorFactory comparatorFactory) {
        this.comparatorFactory = checkNotNull(comparatorFactory);
    }

    @Override
    public int compare(GridControlDataDto o1, GridControlDataDto o2) {
        var gridControlDescriptor1 = o1.getDescriptor();
        var gridControlDescriptor2 = o2.getDescriptor();
        if(!gridControlDescriptor1.equals(gridControlDescriptor2)) {
            // Incomparable
            throw new RuntimeException("Incomparable grids");
        }
        var comparator = comparatorFactory.get(gridControlDescriptor1);
        var lexicographical = Comparators.lexicographical(comparator);
        return lexicographical.compare(o1.getRows(), o2.getRows());
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlDataMatchCriteria;
import edu.stanford.bmir.protege.web.shared.form.field.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrderingDirection.ASC;
import static edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrderingDirection.DESC;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter implements HasVisibility {

    @Nonnull
    private Optional<FormRegionOrderingDirection> sortOrder = Optional.empty();

    interface ColumnHeaderClickedHandler {
        void handleGridHeaderColumnClicked();
    }

    @Nonnull
    private final GridHeaderCellView view;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Nonnull
    private ColumnHeaderClickedHandler columnHeaderClicked = () -> {};

    @Nonnull
    private Optional<GridColumnDescriptorDto> columnDescriptor = Optional.empty();

    @Nonnull
    private GridColumnFilterPresenter filterPresenter;

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderCellView view,
                                     @Nonnull LanguageMapCurrentLocaleMapper localeMapper,
                                     @Nonnull GridColumnFilterPresenter filterPresenter) {
        this.view = checkNotNull(view);
        this.localeMapper = checkNotNull(localeMapper);
        this.filterPresenter = checkNotNull(filterPresenter);
        this.view.setClickHandler(event -> columnHeaderClicked.handleGridHeaderColumnClicked());
    }

    public void setColumnDescriptor(@Nonnull GridColumnDescriptorDto columnDescriptor) {
        String label = localeMapper.getValueForCurrentLocale(columnDescriptor.getLabel());
        view.setLabel(label);
        filterPresenter.setGridColumnDescriptor(columnDescriptor);
        this.columnDescriptor = Optional.of(columnDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public boolean isPresenterFor(@Nonnull FormRegionId columnId) {
        return columnDescriptor.map(GridColumnDescriptorDto::getId)
                               .map(id -> id.equals(columnId))
                .orElse(false);
    }

    @Override
    public boolean isVisible() {
        return view.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    public void setColumnHeaderClickedHandler(@Nonnull ColumnHeaderClickedHandler handler) {
        this.columnHeaderClicked = checkNotNull(handler);
    }

    public void setSortOrder(@Nonnull FormRegionOrderingDirection direction) {
        this.sortOrder = Optional.of(direction);
        if(direction.equals(ASC)) {
            view.setSortAscending();
        }
        else {
            view.setSortDescending();
        }
    }

    public void clearSortOrder() {
        this.sortOrder = Optional.empty();
        view.clearSortOrder();
    }

    public FormRegionOrderingDirection toggleSortOrder() {
        FormRegionOrderingDirection next = sortOrder.map(o -> {
            if (o.equals(ASC)) {
                return DESC;
            } else {
                return ASC;
            }
        }).orElse(ASC);
        setSortOrder(next);
        return next;
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HasVisibility;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderByDirection;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderByDirection.ASC;
import static edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderByDirection.DESC;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter implements HasVisibility {

    @Nonnull
    private Optional<GridControlOrderByDirection> sortOrder = Optional.empty();

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
    private Optional<GridColumnDescriptor> columnDescriptor = Optional.empty();

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderCellView view,
                                     @Nonnull LanguageMapCurrentLocaleMapper localeMapper) {
        this.view = checkNotNull(view);
        this.localeMapper = checkNotNull(localeMapper);
        this.view.setClickHandler(event -> columnHeaderClicked.handleGridHeaderColumnClicked());
    }

    public void setColumnDescriptor(@Nonnull GridColumnDescriptor columnDescriptor) {
        String label = localeMapper.getValueForCurrentLocale(columnDescriptor.getLabel());
        view.setLabel(label);
        this.columnDescriptor = Optional.of(columnDescriptor);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public boolean isPresenterFor(@Nonnull GridColumnId columnId) {
        return columnDescriptor.map(GridColumnDescriptor::getId)
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

    public void setSortOrder(@Nonnull GridControlOrderByDirection direction) {
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

    public GridControlOrderByDirection toggleSortOrder() {
        GridControlOrderByDirection next = sortOrder.map(o -> {
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

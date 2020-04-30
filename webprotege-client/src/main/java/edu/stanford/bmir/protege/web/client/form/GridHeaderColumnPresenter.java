package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderColumnPresenter {

    @Nonnull
    private final GridHeaderColumnView view;

    @Nonnull
    private final Provider<GridHeaderPresenter> gridHeaderPresenterProvider;

    @Nonnull
    private Optional<GridColumnDescriptor> columnDescriptor = Optional.empty();

    @Nonnull
    private final List<GridHeaderPresenter> hoistedHeaderPresenters = new ArrayList<>();

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Inject
    public GridHeaderColumnPresenter(@Nonnull GridHeaderColumnView view,
                                     @Nonnull Provider<GridHeaderPresenter> gridHeaderPresenterProvider,
                                     @Nonnull LanguageMapCurrentLocaleMapper localeMapper) {
        this.view = checkNotNull(view);
        this.gridHeaderPresenterProvider = checkNotNull(gridHeaderPresenterProvider);
        this.localeMapper = checkNotNull(localeMapper);
    }

    public Optional<GridColumnId> getColumnId() {
        return columnDescriptor.map(GridColumnDescriptor::getId);
    }
    
    public void setColumnDescriptor(@Nonnull GridColumnDescriptor columnDescriptor) {
        this.columnDescriptor = Optional.of(columnDescriptor);
        hoistedHeaderPresenters.clear();
        FormControlDescriptor formControlDescriptor = columnDescriptor.getFormControlDescriptor();
        if(formControlDescriptor instanceof GridControlDescriptor) {
            ImmutableList<GridColumnDescriptor> columnDescriptors = ((GridControlDescriptor) formControlDescriptor).getColumns();
            // Hoist up
            GridHeaderPresenter gridHeaderPresenter = gridHeaderPresenterProvider.get();
            gridHeaderPresenter.start(view.getSubHeaderContainer());
            gridHeaderPresenter.setColumns(columnDescriptors);
            hoistedHeaderPresenters.add(gridHeaderPresenter);
        }
        else {
            // Leaf column
            String label = localeMapper.getValueForCurrentLocale(columnDescriptor.getLabel());
            view.setLabel(label);
        }
        // TODO: Width etc
    }

    @Nonnull
    public IsWidget getView() {
        return view;
    }

    @Nonnull
    public ImmutableList<GridColumnDescriptor> getLeafColumns() {
        return columnDescriptor.map(this::getLeafColumnIds).orElse(ImmutableList.of());
    }

    @Nonnull
    public ImmutableList<GridColumnDescriptor> getLeafColumnIds(GridColumnDescriptor cd) {
        if(hoistedHeaderPresenters.isEmpty()) {
            return ImmutableList.of(cd);
        }
        else {
            return hoistedHeaderPresenters.stream()
                                   .flatMap(headerPresenter -> headerPresenter.getLeafColumns().stream())
                                   .collect(toImmutableList());
        }
    }
}

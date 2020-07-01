package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.modal.ModalManager;
import edu.stanford.bmir.protege.web.client.library.modal.ModalPresenter;
import edu.stanford.bmir.protege.web.shared.form.data.FormRegionFilter;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptorDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.*;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-17
 */
public class GridFilterPresenter {

    @Nonnull
    private final GridFilterView view;

    @Nonnull
    private final ModalManager modalManager;

    @Nonnull
    private final FormControlFilterMapper formControlFilterMapper;

    @Nonnull
    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Nonnull
    private final FormsMessages formsMessages;

    @Nonnull
    private final Map<FormRegionId, FormControlFilterPresenter> filterPresenters = new LinkedHashMap<>();

    @Nonnull
    private ImmutableList<GridColumnDescriptorDto> currentColumns = ImmutableList.of();

    @Inject
    public GridFilterPresenter(@Nonnull GridFilterView view,
                               @Nonnull ModalManager modalManager,
                               @Nonnull FormControlFilterMapper formControlFilterMapper,
                               @Nonnull LanguageMapCurrentLocaleMapper localeMapper,
                               @Nonnull FormsMessages formsMessages) {
        this.view = checkNotNull(view);
        this.modalManager = checkNotNull(modalManager);
        this.formControlFilterMapper = checkNotNull(formControlFilterMapper);
        this.localeMapper = checkNotNull(localeMapper);
        this.formsMessages = checkNotNull(formsMessages);
        this.view.setClearFiltersHandler(this::clearFilters);
    }

    public void clear() {
        view.clear();
        filterPresenters.clear();
    }

    public void showModal(@Nonnull ImmutableList<GridColumnDescriptorDto> columns,
                          @Nonnull ImmutableList<FormRegionFilter> filters,
                          @Nonnull Consumer<ImmutableList<FormRegionFilter>> applyFiltersHandler,
                          @Nonnull Runnable cancelHandler) {
        setColumns(columns);
        setFilters(filters);
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setView(view);
        modalPresenter.setTitle(formsMessages.grid_columnFilter());
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
            ImmutableList<FormRegionFilter> editedFilters = getFilters();
            applyFiltersHandler.accept(editedFilters);
        });
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setButtonHandler(DialogButton.CANCEL, closer -> {
            closer.closeModal();
            cancelHandler.run();
        });
        modalManager.showModal(modalPresenter);
    }

    private void clearFilters() {
        filterPresenters.values().forEach(FormControlFilterPresenter::clear);
    }

    private void setFilters(ImmutableList<FormRegionFilter> filters) {
        filters.forEach(filter -> {
            FormControlFilterPresenter presenter = filterPresenters.get(filter.getFormRegionId());
            if(presenter != null) {
                presenter.setFilter(filter);
            }
        });
    }

    private void setColumns(@Nonnull ImmutableList<GridColumnDescriptorDto> columns) {
        if(this.currentColumns.equals(columns)) {
            return;
        }
        this.currentColumns = columns;
        clear();
        columns.forEach(col -> {
            String columnName = localeMapper.getValueForCurrentLocale(col.getLabel());
            AcceptsOneWidget filterContainer = view.addFilter(columnName);
            Optional<FormControlFilterPresenter> presenter = formControlFilterMapper.getPresenter(col.getFormControlDescriptor());
            presenter.ifPresent(thePresenter -> {
                filterPresenters.put(col.getId(), thePresenter);
                thePresenter.start(filterContainer);
            });

        });
    }

    private ImmutableList<FormRegionFilter> getFilters() {
        ImmutableList.Builder<FormRegionFilter> filterBuilder = ImmutableList.builder();
        filterPresenters.forEach((id, filterPresenter)
                                         -> filterPresenter.getFilter()
                                                           .map(filterCriteria -> FormRegionFilter.get(id, filterCriteria))
                                                           .ifPresent(filterBuilder::add));
        return filterBuilder.build();
    }
}

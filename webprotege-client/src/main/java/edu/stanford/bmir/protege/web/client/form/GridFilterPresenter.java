package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
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
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

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

    @Inject
    public GridFilterPresenter(@Nonnull GridFilterView view, @Nonnull ModalManager modalManager, @Nonnull FormControlFilterMapper formControlFilterMapper, @Nonnull LanguageMapCurrentLocaleMapper localeMapper) {
        this.view = checkNotNull(view);
        this.modalManager = checkNotNull(modalManager);
        this.formControlFilterMapper = checkNotNull(formControlFilterMapper);
        this.localeMapper = checkNotNull(localeMapper);
    }

    public void showModal(@Nonnull ImmutableList<GridColumnDescriptorDto> columns,
                          @Nonnull ImmutableList<FormRegionFilter> filters,
                          @Nonnull Consumer<ImmutableList<FormRegionFilter>> applyFiltersHandler,
                          @Nonnull Runnable cancelHandler) {
        view.clear();
        Map<FormRegionId, FormControlFilterPresenter> filterPresenters = new LinkedHashMap<>();
        columns.forEach(col -> {
            String columnName = localeMapper.getValueForCurrentLocale(col.getLabel());
            AcceptsOneWidget filterContainer = view.addFilter(columnName);
            FormControlFilterPresenter presenter = formControlFilterMapper.getPresenter(col.getFormControlDescriptor());
            if (presenter != null) {
                filterPresenters.put(col.getId(), presenter);
                presenter.start(filterContainer);
            }

        });
        ModalPresenter modalPresenter = modalManager.createPresenter();
        modalPresenter.setView(view);
        // TODO Messages
        modalPresenter.setTitle("Column  Filter");
        modalPresenter.setPrimaryButton(DialogButton.OK);
        modalPresenter.setButtonHandler(DialogButton.OK, closer -> {
            closer.closeModal();
            ImmutableList.Builder<FormRegionFilter> filterBuilder = ImmutableList.builder();
            filterPresenters.forEach((id, filterPresenter) -> {
                filterPresenter.getFilter()
                .map(filterCriteria -> FormRegionFilter.get(id, filterCriteria))
                .ifPresent(filterBuilder::add);
            });
            applyFiltersHandler.accept(filterBuilder.build());
        });
        modalPresenter.setEscapeButton(DialogButton.CANCEL);
        modalPresenter.setButtonHandler(DialogButton.CANCEL, closer -> {
            closer.closeModal();
            cancelHandler.run();
        });
        modalManager.showModal(modalPresenter);
    }
}

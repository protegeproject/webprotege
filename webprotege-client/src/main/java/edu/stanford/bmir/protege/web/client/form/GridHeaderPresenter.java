package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderPresenter {

    @Nonnull
    private final GridHeaderView view;

    @Nonnull
    private final Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider;

    @Nonnull
    private final List<GridHeaderColumnPresenter> columnPresenters = new ArrayList<>();

    @Inject
    public GridHeaderPresenter(@Nonnull GridHeaderView view,
                               @Nonnull Provider<GridHeaderColumnPresenter> headerColumnPresenterProvider) {
        this.view = checkNotNull(view);
        this.headerColumnPresenterProvider = checkNotNull(headerColumnPresenterProvider);
    }

    void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {
        view.clear();
        columnPresenters.clear();;
    }

    public void setColumns(@Nonnull ImmutableList<GridColumnDescriptor> columnDescriptors) {
        clear();
        columnDescriptors.forEach(columnDescriptor -> {
            GridHeaderColumnPresenter columnPresenter = headerColumnPresenterProvider.get();
            columnPresenter.setColumnDescriptor(columnDescriptor);
            IsWidget headerColumnView = columnPresenter.getView();
            view.addColumnHeader(headerColumnView);
            columnPresenters.add(columnPresenter);
        });
    }
}

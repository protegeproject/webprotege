package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridRowPresenter {

    @Nonnull
    private final GridRowView view;

    @Nonnull
    private final Provider<GridCellPresenter> cellPresenterProvider;

    @Nonnull
    private final List<GridCellPresenter> cellPresenters = new ArrayList<>();

    private ImmutableList<GridColumnDescriptor> columnDescriptors = ImmutableList.of();

    @Inject
    public GridRowPresenter(@Nonnull GridRowView view,
                            Provider<GridCellPresenter> cellPresenterProvider) {
        this.view = checkNotNull(view);
        this.cellPresenterProvider = checkNotNull(cellPresenterProvider);
    }

    @Nonnull
    public FormDataValue getFormDataValue() {
        Map<String, FormDataValue> map =
                cellPresenters.stream()
                              .filter(GridCellPresenter::isPresent)
                              .collect(toMap(
                                      presenter -> presenter.getIdOrThrow()
                                                            .getId(),
                                      GridCellPresenter::getValueOrThrow
                              ));
        return new FormDataObject(map);
    }

    public void requestFocus() {
        cellPresenters.stream()
                      .findFirst()
                      .ifPresent(cellPresenter -> cellPresenter.requestFocus());
    }

    public void setColumnDescriptors(ImmutableList<GridColumnDescriptor> columnDescriptors) {
        if(this.columnDescriptors.equals(columnDescriptors)) {
            return;
        }
        cellPresenters.clear();
        view.clear();
        this.columnDescriptors = checkNotNull(columnDescriptors);
        this.columnDescriptors.forEach(column -> {
            GridCellPresenter cellPresenter = cellPresenterProvider.get();
            AcceptsOneWidget cellContainer = view.addCell();
            cellPresenter.start(cellContainer);
            cellPresenter.setDescriptor(column);
            cellPresenters.add(cellPresenter);
        });

    }

    public void setValue(FormDataObject formDataObject) {
        cellPresenters.forEach(cellPresenter -> {
            cellPresenter.getId()
                         .ifPresent(id -> {
                             formDataObject.get(id.getId())
                                           .ifPresent(cellPresenter::setValue);
                         });
        });
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);

    }
}

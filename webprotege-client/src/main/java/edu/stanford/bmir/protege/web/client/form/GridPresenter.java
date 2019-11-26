package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataList;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataObject;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.GridFieldDescriptor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridPresenter {

    @Nonnull
    private final GridView view;

    @Nonnull
    private final Provider<GridRowPresenter> rowPresenterProvider;

    @Nonnull
    private final List<GridRowPresenter> rowPresenters = new ArrayList<>();

    private GridFieldDescriptor descriptor = GridFieldDescriptor.get(ImmutableList.of());

    @Inject
    public GridPresenter(@Nonnull GridView view,
                         @Nonnull Provider<GridRowPresenter> rowPresenterProvider) {
        this.view = checkNotNull(view);
        this.rowPresenterProvider = rowPresenterProvider;
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setDescriptor(GridFieldDescriptor descriptor) {
        if(this.descriptor.equals(descriptor)) {
            return;
        }
        this.descriptor = checkNotNull(descriptor);
        rowPresenters.clear();
    }

    public void setValue(FormDataValue value) {
        rowPresenters.clear();
        // List of objects
        value.asList()
             .forEach(rowDataValue -> {
                 if(rowDataValue instanceof FormDataObject) {
                     FormDataObject formDataObject = (FormDataObject) rowDataValue;
                     GridRowPresenter rowPresenter = rowPresenterProvider.get();
                     AcceptsOneWidget rowContainer = view.addRow();
                     rowPresenter.start(rowContainer);
                     rowPresenter.setColumnDescriptors(descriptor.getColumns());
                     rowPresenter.setValue(formDataObject);
                 }
             });
    }

    public FormDataValue getValue() {
        List<FormDataValue> dataValues = rowPresenters.stream()
                     .map(GridRowPresenter::getFormDataValue)
                     .collect(Collectors.toList());
        return FormDataList.of(dataValues);
    }


}

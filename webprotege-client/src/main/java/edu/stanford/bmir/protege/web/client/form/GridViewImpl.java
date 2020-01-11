package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridViewImpl extends Composite implements GridView {

    private Optional<NewRowHandler> newRowHandler = Optional.empty();

    interface GridViewImplUiBinder extends UiBinder<HTMLPanel, GridViewImpl> {

    }

    private static GridViewImplUiBinder ourUiBinder = GWT.create(GridViewImplUiBinder.class);

    @UiField
    SimplePanel headerContainer;

    @UiField(provided = true)
    ValueListEditor<GridRowPresenter> rowEditor;

    @Inject
    public GridViewImpl(Provider<GridRowPresenter> rowPresenterProvider) {
        rowEditor = new ValueListFlexEditorImpl<>(() -> {
            GridRowPresenterAdapter gridRowPresenterAdapter = new GridRowPresenterAdapter();
            newRowHandler.ifPresent(handler -> {
                GridRowPresenter presenter = handler.createRow();
                gridRowPresenterAdapter.setValue(presenter);
            });
            return gridRowPresenterAdapter;
        });
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getHeaderContainer() {
        return headerContainer;
    }

    @Override
    public void requestFocus() {
        rowEditor.requestFocus();
    }

    @Override
    public void setRows(@Nonnull List<GridRowPresenter> rowPresenters) {
        rowEditor.setValue(rowPresenters);
    }

    @Nonnull
    @Override
    public List<GridRowPresenter> getRows() {
        return rowEditor.getValue().orElse(ImmutableList.of());
    }

    @Override
    public void setNewRowHandler(@Nonnull NewRowHandler newRowHandler) {
        this.newRowHandler = Optional.of(checkNotNull(newRowHandler));
    }

    @Override
    public void clear() {
        rowEditor.clearValue();
    }
}

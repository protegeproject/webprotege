package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.FormControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class GridCellPresenter implements HasRequestFocus {

    @Nonnull
    private final GridCellView view;

    @Nonnull
    private Optional<GridColumnDescriptor> descriptor = Optional.empty();

    @Nonnull
    private final FormControlFactory formControlFactory;

    private Optional<ValueEditor<FormDataValue>> editor = Optional.empty();

    @Inject
    public GridCellPresenter(@Nonnull GridCellView view,
                             @Nonnull FormControlFactory formControlFactory) {
        this.view = checkNotNull(view);
        this.formControlFactory = formControlFactory;
    }

    public Optional<GridColumnId> getId() {
        return descriptor.map(GridColumnDescriptor::getId);
    }

    public void requestFocus() {
        view.requestFocus();
    }

    public void setDescriptor(GridColumnDescriptor column) {
        FormControlDescriptor formControlDescriptor = column.getFieldDescriptor();
        Optional<ValueEditorFactory<FormDataValue>> valueEditorFactory = formControlFactory.getValueEditorFactory(
                formControlDescriptor);
        editor = valueEditorFactory.map(ValueEditorFactory::createEditor);
        editor.ifPresent(e -> {
            view.getEditorContainer().setWidget(e);
        });
        this.descriptor = Optional.of(column);
    }

    public void start(AcceptsOneWidget cellContainer) {
        cellContainer.setWidget(view);
    }

    public void setValue(FormDataValue value) {
        editor.ifPresent(e -> e.setValue(value));
    }

    public Optional<FormDataValue> getValue() {
        return editor.flatMap(ValueEditor::getValue);
    }

    public boolean isPresent() {
        return getId().isPresent() && getValue().isPresent();
    }

    public GridColumnId getIdOrThrow() {
        return getId().orElseThrow(NoSuchElementException::new);
    }

    public FormDataValue getValueOrThrow() {
        return getValue().orElseThrow(NoSuchElementException::new);
    }
}

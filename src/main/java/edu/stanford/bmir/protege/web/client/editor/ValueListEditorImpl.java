package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.HasDeleteable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/06/2013
 */
public class ValueListEditorImpl<O> extends Composite implements ValueListEditor<O>, HasPlaceholder {

    public static final int DELETE_BUTTON_COLUMN = 1;
    private final DirtyChangedHandler dirtyChangedHandler;

    private final ValueChangeHandler<Optional<O>> valueChangeHandler;

    interface ValueListEditorImplUiBinder extends UiBinder<HTMLPanel, ValueListEditorImpl<?>> {

    }

    private static ValueListEditorImplUiBinder ourUiBinder = GWT.create(ValueListEditorImplUiBinder.class);


    private ValueEditorFactory<O> valueEditorFactory;

    private List<ValueEditor<O>> currentEditors = new ArrayList<ValueEditor<O>>();

    private boolean dirty = false;

    private boolean enabled = false;

    private String placeholder = "";

    @UiField
    protected FlexTable tableField;

    public ValueListEditorImpl(ValueEditorFactory<O> valueEditorFactory) {
        this.valueEditorFactory = valueEditorFactory;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        valueChangeHandler = event -> handleValueEditorValueChanged();
        dirtyChangedHandler = event -> handleValueEditorDirtyChanged(event);
        tableField.setBorderWidth(0);
        tableField.setCellPadding(0);
        tableField.setCellSpacing(0);
        ensureBlank();
        updateEnabled();
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = checkNotNull(placeholder);
    }

    @Override
    public Widget getWidget() {
        return this;
    }


    @Override
    public void setValue(List<O> object) {
        clearInternal();
        for(O value : object) {
            ValueEditor<O> editor = addValueEditor(true);
            editor.setValue(value);
        }
        ensureBlank();
        updateEnabled();
        dirty = false;
    }

    private void clearInternal() {
        tableField.removeAllRows();
        currentEditors.clear();
        dirty = false;
    }

    @Override
    public void clearValue() {
        clearInternal();
        ensureBlank();
        dirty = false;
    }

    @Override
    public Optional<List<O>> getValue() {
        List<O> editedValues = new ArrayList<O>();
        for(ValueEditor<O> editor : currentEditors) {
            Optional<O> value = editor.getValue();
            if(value.isPresent() && editor.isWellFormed()) {
                editedValues.add(value.get());
            }
        }
        return Optional.of(editedValues);
    }


    @Override
    public boolean isWellFormed() {
        for(ValueEditor<O> editor : currentEditors) {
            if(!editor.isWellFormed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDirty() {
        for(ValueEditor<O> editor : currentEditors) {
            if(editor.isDirty()) {
                return true;
            }
        }
        return dirty;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        updateEnabled();
    }

    private void updateEnabled() {
        for(ValueEditor<O> editor : currentEditors) {
            if(editor instanceof HasEnabled) {
                ((HasEnabled) editor).setEnabled(enabled);
            }
        }
        for(int i = 0; i < tableField.getRowCount(); i++) {
            ValueEditor<?> editor = currentEditors.get(i);
            boolean deletable = true;
            if(editor instanceof HasDeleteable) {
                GWT.log("[ValueListEditorImpl] Implements HasDeleteable");
                deletable = ((HasDeleteable) editor).isDeleteable();
            }
            DeleteButton deleteButton = (DeleteButton) tableField.getWidget(i, DELETE_BUTTON_COLUMN);
            deleteButton.setEnabled(enabled && deletable);
            // Don't enabled the delete button for the last row if it is a blank row
            if (i < tableField.getRowCount() - 1 || !enabled) {
                deleteButton.setVisible(enabled);
            }
        }
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<List<O>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }



    private void ensureBlank() {
        if (isEnabled()) {
            if(currentEditors.isEmpty() || currentEditors.get(currentEditors.size() - 1).getValue().isPresent()) {
                addValueEditor(false);
            }
        }
        for(int i = 0; i < tableField.getRowCount(); i++) {
            ValueEditor<?> currentEditor = currentEditors.get(i);
            boolean deletable = true;
            if(currentEditor instanceof HasDeleteable) {
                deletable = ((HasDeleteable) currentEditor).isDeleteable();
            }
            DeleteButton deleteButton = (DeleteButton) tableField.getWidget(i, DELETE_BUTTON_COLUMN);
            if(i < tableField.getRowCount() - 1 && !deleteButton.isVisible()) {
                deleteButton.setEnabled(enabled && deletable);
                deleteButton.setVisible(enabled);
            }
        }
    }

    private ValueEditor<O> addValueEditor(boolean deleteVisibleByDefault) {
        GWT.log("[ValueListEditorImpl] Adding value list editor");
        final ValueEditor<O> editor = getFreshValueEditor();
        boolean deletable = deleteVisibleByDefault;
        GWT.log("[ValueListEditorImpl] Editor class: " + editor.getClass().getName());
        GWT.log("[ValueListEditorImpl] HasDeleteable: " + (editor instanceof HasDeleteable));
        if(deleteVisibleByDefault && editor instanceof HasDeleteable) {
            deletable = ((HasDeleteable) editor).isDeleteable();
        }
        currentEditors.add(editor);
        final int rowCount = tableField.getRowCount();
        tableField.setWidget(rowCount, 0, editor.asWidget());
        final DeleteButton deleteButton = new DeleteButton();
        deleteButton.addClickHandler(event -> handleDelete(editor));
        tableField.setWidget(rowCount, 1, deleteButton);
        final FlexTable.FlexCellFormatter formatter = tableField.getFlexCellFormatter();
        formatter.setWidth(rowCount, 0, "100%");
        formatter.setVerticalAlignment(rowCount, 0, HasVerticalAlignment.ALIGN_TOP);
        formatter.setWidth(rowCount, 1, "30px");
        formatter.getElement(rowCount, 1).getStyle().setPaddingLeft(1, Style.Unit.PX);
        formatter.setVerticalAlignment(rowCount, 1, HasVerticalAlignment.ALIGN_TOP);
        editor.addDirtyChangedHandler(dirtyChangedHandler);
        editor.addValueChangeHandler(valueChangeHandler);
        deleteButton.setVisible(deletable);
        deleteButton.setEnabled(enabled);
        if(editor instanceof HasEnabled) {
            ((HasEnabled) editor).setEnabled(enabled);
        }
        if(editor instanceof HasPlaceholder) {
            ((HasPlaceholder) editor).setPlaceholder(placeholder);
        }
        return editor;
    }

    private void handleDelete(ValueEditor<O> editor) {
        GWT.log("Handle delete");
        Optional<List<O>> before = getValue();
        removeEditor(editor);
        ensureBlank();
        dirty = true;
        fireEvent(new DirtyChangedEvent());
        Optional<List<O>> after = getValue();
        if (!after.equals(before)) {
            ValueChangeEvent.fire(this, after);
        }
    }

    private void removeEditor(ValueEditor<O> editor) {
        int index = currentEditors.indexOf(editor);
        if(index == -1) {
            return;
        }
        currentEditors.remove(editor);
        tableField.removeRow(index);
    }

    private ValueEditor<O> getFreshValueEditor() {
        return valueEditorFactory.createEditor();
    }


    private void handleValueEditorValueChanged() {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
        ensureBlank();
    }

    private void handleValueEditorDirtyChanged(DirtyChangedEvent event) {
        this.fireEvent(event);
        ensureBlank();
    }

}

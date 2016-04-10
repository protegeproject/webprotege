package edu.stanford.bmir.protege.web.client.ui.editor;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class ValueListFlexEditorImpl<O> extends Composite implements ValueListEditor<O>, HasPlaceholder {

    private ValueListFlexEditorDirection direction = ValueListFlexEditorDirection.COLUMN;

    interface ValueListInlineEditorImplUiBinder extends UiBinder<HTMLPanel, ValueListFlexEditorImpl> {

    }

    private static ValueListInlineEditorImplUiBinder ourUiBinder = GWT.create(ValueListInlineEditorImplUiBinder.class);


    private ValueEditorFactory<O> valueEditorFactory;

    private List<ValueEditor<O>> currentEditors = new ArrayList<ValueEditor<O>>();

    private ValueChangeHandler<Optional<O>> valueChangeHandler;

    private DirtyChangedHandler dirtyChangedHandler;

    private boolean dirty = false;

    private boolean enabled = false;

    private String placeholder = "";

    @UiField
    HTMLPanel container;

    public ValueListFlexEditorImpl(ValueEditorFactory<O> valueEditorFactory) {
        this.valueEditorFactory = valueEditorFactory;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        valueChangeHandler = event -> handleValueEditorValueChanged();
        dirtyChangedHandler = event -> handleValueEditorDirtyChanged(event);
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
        container.clear();
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
//        for(ValueEditor<O> editor : currentEditors) {
//            if(editor instanceof HasEnabled) {
//                ((HasEnabled) editor).setEnabled(enabled);
//            }
//        }
        for(int i = 0; i < container.getWidgetCount(); i++) {
            ValueListFlexEditorContainer editorContainer = (ValueListFlexEditorContainer) container.getWidget(i);
            editorContainer.setEnabled(enabled);

            // Don't enabled the delete button for the last row if it is a blank row
            if (i < container.getWidgetCount() - 1 || !enabled) {
                editorContainer.setDeleteButtonVisible(enabled);
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
        updateEnabled();
//        for(int i = 0; i < container.getWidgetCount(); i++) {
//            ValueListFlexEditorContainer editorContainer = (ValueListFlexEditorContainer) container.getWidget(i);
//            if(i < container.getWidgetCount() - 1 && !editorContainer.isDeleteButtonVisible()) {
//                editorContainer.setDeleteButtonEnabled(enabled);
//                editorContainer.setDeleteButtonVisible(enabled);
//            }
//        }
    }

    private ValueEditor<O> addValueEditor(boolean deleteVisible) {
        final ValueEditor<O> editor = getFreshValueEditor();
        currentEditors.add(editor);
        ValueListFlexEditorContainer<O> editorContainer = new ValueListFlexEditorContainer<>(editor);
        container.add(editorContainer);
        editorContainer.setDirection(direction);

        // TODO: Add delete button handler
        editor.addDirtyChangedHandler(dirtyChangedHandler);
        editor.addValueChangeHandler(valueChangeHandler);
        editorContainer.setDeleteButtonVisible(deleteVisible);
        editorContainer.setDeleteButtonEnabled(enabled);

        if(editor instanceof HasEnabled) {
            ((HasEnabled) editor).setEnabled(enabled);
        }
        if(editor instanceof HasPlaceholder) {
            ((HasPlaceholder) editor).setPlaceholder(placeholder);
        }
        return editor;
    }

    public void setDirection(ValueListFlexEditorDirection direction) {
        this.direction = direction;
        Style style = getElement().getStyle();
        if(direction == ValueListFlexEditorDirection.COLUMN) {
            style.setProperty("flexDirection", "column");
            style.clearProperty("flexFlow");
            style.clearMargin();
        }
        else {
            style.setProperty("flexDirection", "row");
            style.setProperty("flexFlow", "wrap");
            style.setMargin(-5, Style.Unit.PX);
        }
        for(int i = 0; i < container.getWidgetCount(); i++) {
            ValueListFlexEditorContainer<O> editorContainer = (ValueListFlexEditorContainer<O>) container.getWidget(i);
            editorContainer.setDirection(direction);
        }
    }

    private void handleDelete(ValueEditor<O> editor) {
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
        container.remove(index);
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
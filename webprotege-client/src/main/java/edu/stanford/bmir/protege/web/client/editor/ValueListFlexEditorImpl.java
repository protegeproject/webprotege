package edu.stanford.bmir.protege.web.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.ui.ElementalUtil;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import elemental.dom.Element;
import elemental.events.EventRemover;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/04/16
 */
public class ValueListFlexEditorImpl<O> extends Composite implements ValueListEditor<O>, HasPlaceholder, HasEnabled {

    private static ValueListInlineEditorImplUiBinder ourUiBinder = GWT.create(ValueListInlineEditorImplUiBinder.class);

    @UiField
    FlowPanel container;

    @UiField
    Button addButton;

    @UiField
    Button moveUpButton;

    @UiField
    Button moveDownButton;

    private ValueListFlexEditorDirection direction = ValueListFlexEditorDirection.COLUMN;

    private ValueEditorFactory<O> valueEditorFactory;

    private List<ValueEditor<O>> currentEditors = new ArrayList<>();

    private ValueChangeHandler<Optional<O>> valueChangeHandler;

    private DirtyChangedHandler dirtyChangedHandler;

    private boolean dirty = false;

    private boolean enabled = false;

    private String placeholder = "";

    private NewRowMode newRowMode = NewRowMode.AUTOMATIC;

    // Don't prompt by default
    private DeleteConfirmationPrompt<O> deleteConfirmationPrompt = (value, callback) -> callback.deleteValue(true);

    private EventRemover focusInEventRemover = () -> {
    };

    private EventRemover focusOutEventRemover = () -> {
    };

    public ValueListFlexEditorImpl(ValueEditorFactory<O> valueEditorFactory) {
        this.valueEditorFactory = valueEditorFactory;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        valueChangeHandler = event -> handleValueEditorValueChanged();
        dirtyChangedHandler = event -> handleValueEditorDirtyChanged(event);
        updateEnabled();
        moveUpButton.addMouseDownHandler(mouseDownEvent -> {
            mouseDownEvent.preventDefault();
            handleMoveRowUp();
        });
        moveDownButton.addMouseDownHandler(mouseDownEvent -> {
            mouseDownEvent.preventDefault();
            handleMoveRowDown();
        });
    }

    @UiHandler("addButton")
    public void addButtonClick(ClickEvent event) {
        if(!enabled) {
            return;
        }
        ValueEditor<O> valueEditor = addValueEditor(true);
        if(valueEditor instanceof HasRequestFocus) {
            Scheduler.get()
                     .scheduleDeferred(((HasRequestFocus) valueEditor)::requestFocus);
        }
        updateEnabled();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<List<O>>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
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
        editorContainer.setDeleteButtonClickedHandler(event -> handleDelete(editor));


        if(editor instanceof HasEnabled) {
            ((HasEnabled) editor).setEnabled(enabled);
        }
        if(editor instanceof HasPlaceholder) {
            ((HasPlaceholder) editor).setPlaceholder(placeholder);
        }
        return editor;
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

    private void ensureBlank() {
        if(isEnabled()) {
            if((newRowMode == NewRowMode.AUTOMATIC && (currentEditors.isEmpty() || currentEditors.get(currentEditors.size() - 1)
                                                                                                 .getValue()
                                                                                                 .isPresent()))) {
                addValueEditor(false);
            }
        }
    }

    @Override
    public void firstEditor(@Nonnull Consumer<ValueEditor<O>> consumer) {
        currentEditors.stream()
                      .findFirst()
                      .ifPresent(consumer);
    }

    @Override
    public void forEachEditor(@Nonnull Consumer<ValueEditor<O>> consumer) {
        currentEditors.forEach(consumer);
    }

    private Optional<ValueEditor<O>> getFocusedEditor() {
        return currentEditors.stream()
                             .filter(ElementalUtil::isWidgetOrDescendantWidgetActive)
                             .findFirst();
    }

    private ValueEditor<O> getFreshValueEditor() {
        return valueEditorFactory.createEditor();
    }

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.placeholder = checkNotNull(placeholder);
        currentEditors.stream()
                      .filter(editor -> editor instanceof HasPlaceholder)
                      .map(editor -> (HasPlaceholder) editor)
                      .forEach(hasPlaceholder -> hasPlaceholder.setPlaceholder(placeholder));
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    private void handleDelete(ValueEditor<O> editor) {
        Optional<O> value = editor.getValue();
        if(!value.isPresent()) {
            return;
        }
        deleteConfirmationPrompt.shouldDeleteValue(value.get(), delete -> {
            if(delete) {
                Optional<List<O>> before = getValue();
                int remIndex = removeEditor(editor);
                ensureBlank();
                dirty = true;
                fireEvent(new DirtyChangedEvent());
                Optional<List<O>> after = getValue();
                if(!after.equals(before)) {
                    ValueChangeEvent.fire(this, after);
                }
                if(remIndex != -1) {
                    int nextIndex = Math.min(remIndex, container.getWidgetCount() - 1);
                    if(nextIndex != -1) {
                        ValueListFlexEditorContainer<O> editorContainer = (ValueListFlexEditorContainer<O>) container.getWidget(
                                nextIndex);
                        editorContainer.deleteButton.setFocus(true);
                    }
                }
            }
        });
    }

    private void handleMoveRowDown() {
        getFocusedEditor().ifPresent(this::handleMoveRowDown);
    }

    private void handleMoveRowDown(ValueEditor<O> editor) {
        int currentIndex = currentEditors.indexOf(editor);
        if(currentIndex > -1 && currentIndex < currentEditors.size() - 1) {
            currentEditors.remove(currentIndex);
            currentEditors.add(currentIndex + 1, editor);
            ValueListFlexEditorContainer<O> editorContainer = (ValueListFlexEditorContainer<O>) container.getWidget(
                    currentIndex + 1);
            container.remove(editorContainer);
            container.insert(editorContainer, currentIndex);
        }
    }

    private void handleMoveRowUp() {
        getFocusedEditor().ifPresent(this::handleMoveRowUp);
    }

    private void handleMoveRowUp(ValueEditor<O> editor) {
        int currentIndex = currentEditors.indexOf(editor);
        if(currentIndex > 0) {
            currentEditors.remove(currentIndex);
            currentEditors.add(currentIndex - 1, editor);
            ValueListFlexEditorContainer<O> editorContainer = (ValueListFlexEditorContainer<O>) container.getWidget(
                    currentIndex - 1);
            container.remove(editorContainer);
            container.insert(editorContainer, currentIndex);
        }
    }

    private void handleValueEditorDirtyChanged(DirtyChangedEvent event) {
        this.fireEvent(event);
        ensureBlank();
    }

    private void handleValueEditorValueChanged() {
        dirty = true;
        ValueChangeEvent.fire(this, getValue());
        updateEnabled();
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
    protected void onAttach() {
        super.onAttach();
        Element element = (Element) getElement();
        focusInEventRemover = element.addEventListener(BrowserEvents.FOCUSIN, evt -> {
            updateReorderButtonStates();
        });
        focusOutEventRemover = element.addEventListener(BrowserEvents.FOCUSOUT, evt -> {
            updateReorderButtonStates();
        });
        updateReorderButtonStates();
    }

    @Override
    protected void onDetach() {
        focusInEventRemover.remove();
        focusOutEventRemover.remove();
        super.onDetach();
    }

    private int removeEditor(ValueEditor<O> editor) {
        int index = currentEditors.indexOf(editor);
        if(index == -1) {
            return index;
        }
        currentEditors.remove(editor);
        container.remove(index);
        return index;
    }

    @Override
    public void requestFocus() {
        if(currentEditors.isEmpty()) {
            addButton.setFocus(true);
        }
        else {
            ValueEditor<O> firstEditor = currentEditors.get(0);
            if(firstEditor instanceof HasRequestFocus) {
                ((HasRequestFocus) firstEditor).requestFocus();
            }
        }
    }

    @Override
    public void setDeleteConfirmationPrompt(@Nonnull DeleteConfirmationPrompt<O> prompt) {
        this.deleteConfirmationPrompt = checkNotNull(prompt);
    }

    public void setDirection(ValueListFlexEditorDirection direction) {
        this.direction = direction;
        Style style = container.getElement()
                               .getStyle();
        if(direction == ValueListFlexEditorDirection.COLUMN) {
            style.setProperty("flexDirection", "column");
            style.setProperty("flexWrap", "noWrap");
            style.clearMargin();
        }
        else {
            style.setProperty("flexDirection", "row");
            style.setProperty("flexWrap", "wrap");
            style.setMargin(-5, Style.Unit.PX);
        }
        for(int i = 0; i < container.getWidgetCount(); i++) {
            ValueListFlexEditorContainer<O> editorContainer = (ValueListFlexEditorContainer<O>) container.getWidget(i);
            editorContainer.setDirection(direction);
        }
    }

    @Override
    public void setNewRowMode(@Nonnull NewRowMode newRowMode) {
        this.newRowMode = checkNotNull(newRowMode);
        if(newRowMode == NewRowMode.MANUAL) {
            if(enabled) {
                addButton.setVisible(true);
            }
        }
        else {
            addButton.setVisible(false);
        }
    }

    @Override
    public void setReorderEnabled(boolean enabled) {
        moveUpButton.setVisible(enabled);
        moveDownButton.setVisible(enabled);
        updateReorderButtonStates();
    }

    private void updateReorderButtonStates() {
        boolean enable = getValue().map(v -> v.size() > 1)
                                   .orElse(false) && ElementalUtil.isWidgetOrDescendantWidgetActive(this);
        moveUpButton.setEnabled(enable);
        moveDownButton.setEnabled(enable);
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

    private void updateEnabled() {
        for(int i = 0; i < container.getWidgetCount(); i++) {
            ValueListFlexEditorContainer editorContainer = (ValueListFlexEditorContainer) container.getWidget(i);
            editorContainer.setEnabled(enabled);

            // Don't enabled the delete button for the last row if it is a blank row
            if(i < container.getWidgetCount() - 1 || currentEditors.get(i)
                                                                   .getValue()
                                                                   .isPresent() || !enabled) {
                editorContainer.setDeleteButtonVisible(enabled);
            }
        }
        if(newRowMode == NewRowMode.MANUAL) {
            addButton.setVisible(enabled);
        }
        ensureBlank();
    }

    interface ValueListInlineEditorImplUiBinder extends UiBinder<HTMLPanel, ValueListFlexEditorImpl> {

    }
}

package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;
import edu.stanford.bmir.protege.web.client.ui.library.common.EventStrategy;
import edu.stanford.bmir.protege.web.client.ui.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.OWLPrimitiveDataList;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/12/2012
 */
public class PrimitiveDataListEditor extends SimplePanel implements HasPlaceholder, HasWidgets, ValueEditor<OWLPrimitiveDataList>, HasEnabled  {


    private FlexTable flexTable;

    private OWLPrimitiveDataList currentValue;

    private ProjectId projectId;

    private FreshEntitiesHandler freshEntitiesHandler = new MutableFreshEntitiesHandler();

    private SortedSet<PrimitiveType> allowedTypes = new TreeSet<PrimitiveType>();

    private String defaultPlaceholder = "";

    private boolean enabled;

    private boolean dirty;

    public PrimitiveDataListEditor(ProjectId projectId, PrimitiveType ... types) {
        this.projectId = projectId;
        this.allowedTypes.addAll(Arrays.asList(types));
        flexTable = new FlexTable();
        setWidget(flexTable);
        ensureBlankRow();
        setWidth("100%");
        flexTable.setWidth("100%");
    }

    @Override
    public String getPlaceholder() {
        return defaultPlaceholder;
    }

    @Override
    public void setPlaceholder(String placeholder) {
        this.defaultPlaceholder = placeholder;
        for(int i = 0; i < flexTable.getRowCount(); i++) {
            Widget w = flexTable.getWidget(i, 0);
            if(w instanceof HasPlaceholder) {
                ((HasPlaceholder) w).setPlaceholder(defaultPlaceholder);
            }
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    @Override
    public boolean isWellFormed() {
        for(int i = 0; i < flexTable.getRowCount(); i++) {
            Optional<OWLPrimitiveData> value = getValueAt(i);
            if(!isEmpty(i) && !value.isPresent()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the widget is enabled, false if not.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether this widget is enabled.
     * @param enabled <code>true</code> to enable the widget, <code>false</code>
     * to disable it
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for(int i = 0; i < flexTable.getRowCount(); i++) {
            Widget w = flexTable.getWidget(i, 0);
            if(w instanceof HasEnabled) {
                ((HasEnabled) w).setEnabled(enabled);
            }
        }
    }

    @Override
    public void setValue(OWLPrimitiveDataList object) {
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
        currentValue = object;
        freshEntitiesHandler = new MutableFreshEntitiesHandler();
        refill();
    }

    private void refill() {
        flexTable.removeAllRows();
        if(currentValue == null) {
            return;
        }
        for(OWLPrimitiveData data : currentValue.getPrimitiveData()) {
            addRow(Optional.of(data));
        }
        ensureBlankRow();
    }

    private void addRow(Optional<OWLPrimitiveData> data) {
        int row = flexTable.getRowCount();
        final PrimitiveDataEditor editor = createEditor(data);
        editor.setSuggestMode(PrimitiveDataEditorSuggestOracleMode.SUGGEST_CREATE_NEW_ENTITIES);
        editor.setEnabled(enabled);
        flexTable.setWidget(row, 0, editor);
        flexTable.getColumnFormatter().setWidth(0, "100%");
        final DeleteButton deleteButton = createDeleteButton();
        if(!data.isPresent()) {
            deleteButton.setVisible(false);
        }
        editor.setPlaceholder(defaultPlaceholder);
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                if(editor.getValue().isPresent()) {
                    deleteButton.setVisible(true);
                    setDirty(true, EventStrategy.FIRE_EVENTS);
                }
            }
        });
        flexTable.setWidget(row, 1, deleteButton);

        flexTable.getColumnFormatter().setWidth(1, "30px");
    }

    private DeleteButton createDeleteButton() {
        final DeleteButton deleteButton = new DeleteButton();
        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleDelete(deleteButton);
            }
        });
        return deleteButton;
    }

    private void handleDelete(DeleteButton deleteButton) {
        for(int i = 0; i < flexTable.getRowCount(); i++) {
            if(flexTable.getWidget(i, 1) == deleteButton) {
                flexTable.removeRow(i);
                setDirty(true, EventStrategy.FIRE_EVENTS);
                fireValueChangedIfWellFormed();
                break;
            }
        }
    }

    private void fireValueChangedIfWellFormed() {
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    private void ensureBlankRow() {
        int rowCount = flexTable.getRowCount();
        if(rowCount == 0 || !isEmpty(rowCount - 1)) {
            addRow(Optional.<OWLPrimitiveData>absent());
        }
    }

    private boolean isEmpty(int row) {
        PrimitiveDataEditor primitiveDataEditor = (PrimitiveDataEditor) flexTable.getWidget(row, 0);
        return primitiveDataEditor.getText().trim().isEmpty();
    }

    private Optional<OWLPrimitiveData> getValueAt(int row) {
        PrimitiveDataEditor primitiveDataEditor = (PrimitiveDataEditor) flexTable.getWidget(row, 0);
        return primitiveDataEditor.getValue();
    }

    private PrimitiveDataEditor createEditor(Optional<OWLPrimitiveData> value) {

        PrimitiveDataEditor editor = PrimitiveDataEditorGinjector.INSTANCE.getEditor();

//        DefaultPrimitiveDataEditor editor = new DefaultPrimitiveDataEditor(projectId);
        editor.setAllowedTypes(allowedTypes);
        editor.setFreshEntitiesHandler(freshEntitiesHandler);
        if(value.isPresent()) {
            editor.setValue(value.get());
        }
        editor.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
            @Override
            public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                setDirty(true, EventStrategy.FIRE_EVENTS);
                ensureBlankRow();
                fireValueChangedIfWellFormed();
            }
        });
        editor.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    ensureBlankRow();
                }
            }
        });
        return editor;
    }

    @Override
    public void clearValue() {
        setDirty(false, EventStrategy.DO_NOT_FIRE_EVENTS);
        flexTable.removeAllRows();
        ensureBlankRow();
    }

    @Override
    public Optional<OWLPrimitiveDataList> getValue() {
        List<OWLPrimitiveData> list = new ArrayList<OWLPrimitiveData>();
        for(int i = 0; i < flexTable.getRowCount(); i++) {
            Optional<OWLPrimitiveData> data = getValueAt(i);
            if(data.isPresent()) {
                list.add(data.get());
            }
        }
        return Optional.of(new OWLPrimitiveDataList(list));
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    private void setDirty(boolean dirty, EventStrategy eventStrategy) {
        this.dirty = dirty;
        if(eventStrategy == EventStrategy.FIRE_EVENTS) {
            fireEvent(new DirtyChangedEvent());
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<OWLPrimitiveDataList>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}

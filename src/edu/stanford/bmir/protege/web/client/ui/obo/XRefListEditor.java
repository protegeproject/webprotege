package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOXRef;
import edu.stanford.bmir.protege.web.client.ui.library.button.DeleteButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public class XRefListEditor extends FlowPanel implements OBOTermEditor {

    public static final int ADD_BUTTON_COLUMN = 0;

    private final List<XRefEditor> xRefEditorList = new ArrayList<XRefEditor>();

    private final FlexTable editorListTable = new FlexTable();

    private final Button addButton;

    private boolean dirty = false;

    private XRefEditorLayout xRefEditorLayout;

    public XRefListEditor() {
        this(XRefEditorLayout.VERTICAL);
    }

    public XRefListEditor(XRefEditorLayout layout) {
        xRefEditorLayout = layout;
        add(editorListTable);
        addButton = new Button("New XRef", new ClickHandler() {
            public void onClick(ClickEvent event) {
                addBlankValue();
            }
        });
    }

    public boolean isDirty() {
        if(dirty) {
            return true;
        }
        for(XRefEditor editor : xRefEditorList) {
            if(editor.isDirty()) {
                return true;
            }
        }
        return false;
    }

    public void setValues(List<OBOXRef> xrefs) {
        clearList();
        for (OBOXRef xref : xrefs) {
            addXRefEditor(xref);
        }
        if(xrefs.isEmpty()) {
            addXRefEditor(new OBOXRef());
        }
        addAddButton();
        dirty = false;
    }

    public void clearValues() {
        clearList();
        addBlankValue();
        dirty = false;
    }

    public List<OBOXRef> getValues() {
        List<OBOXRef> result = new ArrayList<OBOXRef>();
        for(XRefEditor editor : xRefEditorList) {
            OBOXRef xref = editor.getXRef();
            if(!xref.isEmpty()) {
                result.add(xref);
            }
        }
        return result;
    }




    private void updateAddButton() {
        removeAddButton();
        addAddButton();
    }

    private void addAddButton() {
        int rowCount = editorListTable.getRowCount();
        editorListTable.setWidget(rowCount, ADD_BUTTON_COLUMN, addButton);
        editorListTable.getFlexCellFormatter().setHorizontalAlignment(rowCount, ADD_BUTTON_COLUMN, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    private void removeAddButton() {
        for(int i = 0; i < editorListTable.getRowCount(); i++) {
            if(editorListTable.getWidget(i, ADD_BUTTON_COLUMN) == addButton) {
                editorListTable.removeRow(i);
                break;
            }
        }
    }


    private void addBlankValue() {
        removeAddButton();
        addXRefEditor(new OBOXRef());
        addAddButton();
    }

    private void addXRefEditor(OBOXRef xref) {
        XRefEditor editor = new XRefEditor(xRefEditorLayout);
        editor.setXRef(xref);
        addXRefEditor(editor);
    }


    private void clearList() {
        xRefEditorList.clear();
        editorListTable.removeAllRows();
    }


    private void addXRefEditor(final XRefEditor editor) {
        xRefEditorList.add(editor);
        int rowCount = editorListTable.getRowCount();
        editorListTable.setWidget(rowCount, 0, editor);
        editorListTable.getFlexCellFormatter().setVerticalAlignment(rowCount, 0, HasVerticalAlignment.ALIGN_TOP);
//        if (xRefEditorList.size() > 1) {
            DeleteButton deleteButton = new DeleteButton();
            deleteButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    deleteXRef(editor);
                }
            });
            deleteButton.addStyleName("web-protege-component-group-left-padding");
            editorListTable.setWidget(rowCount, 1, deleteButton);
            editorListTable.getFlexCellFormatter().setVerticalAlignment(rowCount, 1, HasVerticalAlignment.ALIGN_TOP);
//        }
    }


    private void deleteXRef(XRefEditor editor) {
        if (xRefEditorList.size() > 1) {
            int index = xRefEditorList.indexOf(editor);
            if (index == -1) {
                return;
            }
            xRefEditorList.remove(index);
            editorListTable.removeRow(index);
        }
        else {
            xRefEditorList.get(0).setXRef(new OBOXRef());
        }
        dirty = true;
        updateAddButton();
    }

    public Widget getEditorWidget(int index) {
        return editorListTable;
    }

    public int getEditorCount() {
        return 1;
    }

    public String getLabel(int index) {
        return "Term XRef";
    }

    public boolean hasXRefs() {
        return false;
    }

    public XRefListEditor getXRefListEditor() {
        return null;
    }
}

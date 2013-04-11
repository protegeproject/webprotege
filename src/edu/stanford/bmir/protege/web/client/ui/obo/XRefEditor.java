package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBOXRef;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class XRefEditor extends FlowPanel {

    public static final int DATA_BASE_FIELD_VISIBLE_LENGTH = 6;

    private TextBox databaseNameField = new TextBox();
    
    private TextBox databaseIdField = new TextBox();
    
    private TextArea descriptionField = new TextArea();

    private boolean dirty = false;



    public XRefEditor() {
        this(XRefEditorLayout.VERTICAL);
    }

    public XRefEditor(XRefEditorLayout layout) {
        databaseNameField.setVisibleLength(DATA_BASE_FIELD_VISIBLE_LENGTH);
        ChangeHandler dirtyChangeHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                dirty = true;
            }
        };
        databaseNameField.addChangeHandler(dirtyChangeHandler);
        databaseNameField.addStyleName("web-protege-form-layout-editor-input");
        databaseIdField.setVisibleLength(DATA_BASE_FIELD_VISIBLE_LENGTH);
        databaseIdField.addStyleName("web-protege-form-layout-editor-input");
        databaseIdField.addChangeHandler(dirtyChangeHandler);
        descriptionField.setVisibleLines(1);
        descriptionField.addChangeHandler(dirtyChangeHandler);
        descriptionField.addStyleName("web-protege-form-layout-editor-input");

        //        descriptionField.addFocusHandler(new FocusHandler() {
//            public void onFocus(FocusEvent event) {
//                if (descriptionField.getVisibleLines() != 3) {
//                    descriptionField.setVisibleLines(3);
//                }
//            }
//        });

        addStyleName("web-protege-laf");

        InlineLabel databaseNameLabel = new InlineLabel("Database Name");
        databaseNameLabel.addStyleName("web-protege-form-label");

        InlineLabel databaseIdLabel = new InlineLabel("Database ID");
        databaseIdLabel.addStyleName("web-protege-form-label");
        databaseIdLabel.addStyleName("web-protege-form-horizontal-padding");
        databaseIdLabel.addStyleName("web-protege-form-horizontal-component-spacing");

        InlineLabel descriptionLabel = new InlineLabel("Description");
        descriptionLabel.addStyleName("web-protege-form-label");


        FlexTable table = new FlexTable();
        table.setBorderWidth(0);
        table.setCellSpacing(0);
        table.setCellPadding(0);

        table.setWidget(0, 0, databaseNameLabel);
        table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        table.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        table.setWidget(0, 1, databaseNameField);
        table.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        table.setWidget(0, 2, databaseIdLabel);
        table.getFlexCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
        table.getFlexCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
        table.setWidget(0, 3, databaseIdField);
        table.getFlexCellFormatter().setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_TOP);

        int descriptionRow = layout == XRefEditorLayout.VERTICAL ? 1 : 0;
        int descriptionLabelCol = layout == XRefEditorLayout.VERTICAL ? 0 : 4;
        int descriptionEditorCol = layout == XRefEditorLayout.VERTICAL ? 1 : 5;

        table.setWidget(descriptionRow, descriptionLabelCol, descriptionLabel);
        table.getFlexCellFormatter().setHorizontalAlignment(descriptionRow, descriptionLabelCol, HasHorizontalAlignment.ALIGN_RIGHT);
        table.setWidget(descriptionRow, descriptionEditorCol, descriptionField);

        table.getFlexCellFormatter().setColSpan(descriptionRow, descriptionEditorCol, 3);

        table.getFlexCellFormatter().setVerticalAlignment(descriptionRow, descriptionLabelCol, HasVerticalAlignment.ALIGN_TOP);
        table.getFlexCellFormatter().setVerticalAlignment(descriptionRow, descriptionEditorCol, HasVerticalAlignment.ALIGN_TOP);

        add(table);
    }

    public boolean isDirty() {
        return dirty;
    }

    public OBOXRef getXRef() {
        return new OBOXRef(getDatabaseName(), getDatabaseId(), getDatabaseDescription());
    }

    private String getDatabaseDescription() {
        return descriptionField.getText();
    }

    private String getDatabaseId() {
        return databaseIdField.getText();
    }

    private String getDatabaseName() {
        return databaseNameField.getText().trim();
    }

    public void setXRef(OBOXRef xRef) {
        databaseNameField.setText(xRef.getDatabaseName());
        databaseIdField.setText(xRef.getDatabaseId());
        descriptionField.setText(xRef.getDescription());
        dirty = false;
    }
}

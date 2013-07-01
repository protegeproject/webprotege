package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.csv.*;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVImportViewImpl extends Composite implements CSVImportView {

    private static final String COLUMN_NUMBER_COLUMN_WIDTH = "100px";

    private static final CSVImportStyle CSV_IMPORT_STYLE = CSVImportClientBundle.INSTANCE.csvImportStyle();

    static {
        CSV_IMPORT_STYLE.ensureInjected();
    }

    interface CSVImportViewImplUiBinder extends UiBinder<HTMLPanel, CSVImportViewImpl> {

    }

    private static CSVImportViewImplUiBinder ourUiBinder = GWT.create(CSVImportViewImplUiBinder.class);

    @UiField
    protected ListBox importTypeField;

    @UiField
    protected ListBox displayNameField;

    @UiField
    protected CSVGridView csvGridView;

    @UiField
    protected FlexTable flexTable;


    @UiField
    protected FlexTable flexTableHeader;

    public CSVImportViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        for(CSVRowImportType importType : CSVRowImportType.values()) {
            importTypeField.addItem(importType.getDisplayName());
        }
        importTypeField.setSelectedIndex(CSVRowImportType.INDIVIDUAL.ordinal());
        displayNameField.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                csvGridView.clearColumnHighlighting();
                csvGridView.setColumnHighlighted(displayNameField.getSelectedIndex(), true);
            }
        });
        displayNameField.addStyleName(CSV_IMPORT_STYLE.displayNameColumn());
        String s = getClass().getName();

    }

//    @UiHandler("importTypeField")
//    protected void handleImportTypeFieldChanged(ChangeEvent changeEvent) {
//        handleDisplayNameColumnChanged();
//    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameFieldChanged(ChangeEvent changeEvent) {
        handleDisplayNameColumnChanged();
    }

    @UiHandler("displayNameField")
    protected void handleDisplayNameFieldKeyUp(KeyUpEvent keyUpEvent) {
        handleDisplayNameColumnChanged();
    }

    public int getDisplayNameColumnIndex() {
        return displayNameField.getSelectedIndex();
    }


    private void handleDisplayNameColumnChanged() {
        csvGridView.removeColumnStyleName(CSV_IMPORT_STYLE.displayNameColumn());
        csvGridView.clearColumnHighlighting();
        final int selectedIndex = displayNameField.getSelectedIndex();
        csvGridView.scrollColumnToVisible(selectedIndex);
        csvGridView.addColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.displayNameColumn());
        CSVRowImportType importType = CSVRowImportType.values()[importTypeField.getSelectedIndex()];
        if (importType == CSVRowImportType.CLASS) {
//            csvGridView.removeColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.namedIndividualIcon());
//            csvGridView.removeColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.literalIcon());
//            csvGridView.removeColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.numberIcon());
//            csvGridView.addColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.classIcon());
        }
        else {
//            csvGridView.removeColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.classIcon());
//            csvGridView.removeColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.literalIcon());
//            csvGridView.removeColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.numberIcon());
//            csvGridView.addColumnStyleName(selectedIndex, CSV_IMPORT_STYLE.namedIndividualIcon());
        }


//        csvGridView.setHeaderText(selectedIndex,  importType.getDisplayName() + " Name");
    }

    @Override
    public Optional<CSVImportDescriptor> getImportDescriptor() {
        List<CSVColumnDescriptor> columnDescriptors = new ArrayList<CSVColumnDescriptor>();
        for(int rowIndex = 0; rowIndex < flexTable.getRowCount(); rowIndex++) {
            CSVColumnRelationEditorViewImpl relView = (CSVColumnRelationEditorViewImpl) flexTable.getWidget(rowIndex, 1);
            if(!relView.getProperty().isPresent()) {
                continue;
            }
            if(!relView.getColumnType().isPresent()) {
                continue;
            }
            final OWLEntity columnProperty = (OWLEntity) relView.getProperty().get().getObject();
            final ColumnType columnType = relView.getColumnType().get();
            CSVColumnDescriptor csvColumnDescriptor = CSVColumnDescriptor.create(rowIndex, columnProperty, columnType);
            columnDescriptors.add(csvColumnDescriptor);
        }
        return Optional.of(new CSVImportDescriptor(CSVRowImportType.values()[importTypeField.getSelectedIndex()], displayNameField.getSelectedIndex(), columnDescriptors));
    }

    @Override
    public void setCSVGrid(CSVGrid grid) {
        displayNameField.clear();
        for(int i = 0; i < grid.getColumnCount(); i++) {
            displayNameField.addItem(getColumnRendering(i));
        }
        csvGridView.setCSVGrid(grid);
        flexTable.removeAllRows();
        flexTableHeader.setText(0, 0, "Column");
        flexTableHeader.setText(0, 1, "Property name and column type");
        flexTableHeader.getColumnFormatter().setWidth(0, COLUMN_NUMBER_COLUMN_WIDTH);

        for(int i = 0; i < grid.getColumnCount(); i++) {
            final Label label = new Label("Column " + (i + 1));
//            label.getElement().getStyle().setPaddingRight(5, Style.Unit.PX);
            String headerStyle = CSVGridResources.INSTANCE.dataGridStyle().dataGridHeader();
            label.addStyleName(headerStyle);
            label.setWidth(COLUMN_NUMBER_COLUMN_WIDTH);
            label.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);
            label.getElement().getStyle().setPaddingRight(0, Style.Unit.PX);
            flexTable.setWidget(i, 0, label);
            flexTable.getColumnFormatter().setWidth(0, COLUMN_NUMBER_COLUMN_WIDTH);
            flexTable.getRowFormatter().setVerticalAlign(i, HasVerticalAlignment.ALIGN_TOP);
            final CSVColumnRelationEditorViewImpl widget = new CSVColumnRelationEditorViewImpl();
            widget.addFocusHandler(new ColumnHighligherFocusHandler(i));
            final int colIndex = i;
            widget.addValueChangeHandler(new ValueChangeHandler<Optional<OWLPrimitiveData>>() {
                @Override
                public void onValueChange(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
                    if(event.getValue().isPresent()) {
                        csvGridView.setHeaderText(colIndex, event.getValue().get().getBrowserText());
                        csvGridView.addColumnStyleName(colIndex, CSV_IMPORT_STYLE.importedColumn());
                    }
                    else {
                        csvGridView.clearHeaderText(colIndex);
                        csvGridView.removeColumnStyleName(colIndex, CSV_IMPORT_STYLE.importedColumn());
                    }
                    GWT.log("Column relation changed...");
                    csvGridView.removeTDStyleName(colIndex, CSV_IMPORT_STYLE.classIcon());
                    csvGridView.removeTDStyleName(colIndex, CSV_IMPORT_STYLE.namedIndividualIcon());
                    csvGridView.removeTDStyleName(colIndex, CSV_IMPORT_STYLE.numberIcon());
                    csvGridView.removeTDStyleName(colIndex, CSV_IMPORT_STYLE.literalIcon());
                    if(widget.getColumnType().isPresent() && widget.getProperty().isPresent()) {
                        final ColumnType columnType = widget.getColumnType().get();
                        if (columnType.getPrimitiveType() == PrimitiveType.CLASS) {
                            csvGridView.addTDStyleName(colIndex, CSV_IMPORT_STYLE.classIcon());
                        }
                        else if(columnType.getPrimitiveType() == PrimitiveType.NAMED_INDIVIDUAL) {
                            csvGridView.addTDStyleName(colIndex, CSV_IMPORT_STYLE.namedIndividualIcon());
                        }
                        else if(columnType == ColumnType.DOUBLE || columnType == ColumnType.INTEGER) {
                            csvGridView.addTDStyleName(colIndex, CSV_IMPORT_STYLE.numberIcon());
                        }
                        else {
                            csvGridView.addTDStyleName(colIndex, CSV_IMPORT_STYLE.literalIcon());
                        }
                    }
//                    csvGridView.setColumnBold(colIndex, event.getValue().isPresent());
                }
            });
            flexTable.setWidget(i, 1, widget);
        }
        handleDisplayNameColumnChanged();
    }

    private static String getColumnRendering(int i) {
        return "Column " + (i + 1);
    }


    private class ColumnHighligherFocusHandler implements FocusHandler {

        private int columnIndex;

        private ColumnHighligherFocusHandler(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public void onFocus(FocusEvent event) {
            csvGridView.removeColumnStyleName(CSV_IMPORT_STYLE.highlightedColumn());
//            if (columnIndex != getDisplayNameColumnIndex()) {
                csvGridView.addColumnStyleName(columnIndex, CSV_IMPORT_STYLE.highlightedColumn());
                csvGridView.scrollColumnToVisible(columnIndex);
//            }
        }
    }
}
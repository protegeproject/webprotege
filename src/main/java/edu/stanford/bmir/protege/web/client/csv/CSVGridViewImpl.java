package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;
import edu.stanford.bmir.protege.web.shared.csv.CSVRow;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 * <p>
 *     A view for a CSVGrid
 * </p>
 */
public class CSVGridViewImpl extends Composite implements CSVGridView {

    private static final String COLUMN_HIGHLIGHTING_STYLE = "web-protege-highlighted-table-column";

    private static final int COLUMN_WIDTH = 200;

    private static final int PAGE_SIZE = 100;

    interface CSVGridViewImplUiBinder extends UiBinder<HTMLPanel, CSVGridViewImpl> {

    }

    private static CSVGridViewImplUiBinder ourUiBinder = GWT.create(CSVGridViewImplUiBinder.class);

    @UiField(provided = true)
    protected ScrollableDataGrid<CSVRow> dataGrid;

    private final ListDataProvider<CSVRow> provider;


    public CSVGridViewImpl() {
        dataGrid = new ScrollableDataGrid<CSVRow>(PAGE_SIZE, CSVGridResources.INSTANCE);
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        dataGrid.addStyleName("web-protege-no-focus");
        provider = new ListDataProvider<CSVRow>();
        provider.addDataDisplay(dataGrid);
        dataGrid.setPageSize(PAGE_SIZE);
        initWidget(rootElement);
    }

    public void setHeaderText(int index, String headerText) {
        CSVGridHeader header = (CSVGridHeader) dataGrid.getHeader(index);
        header.setOverridingText(headerText);
        dataGrid.redrawHeaders();
    }

    public void clearHeaderText(int index) {
        CSVGridHeader header = (CSVGridHeader) dataGrid.getHeader(index);
        header.clearOverridingText();
        dataGrid.redrawHeaders();
    }



    @Override
    public void setCSVGrid(CSVGrid grid) {
        while(dataGrid.getColumnCount() > 0) {
            dataGrid.removeColumn(0);
        }
        for(int i = 0; i < grid.getColumnCount(); i++) {
            dataGrid.addColumn(new CSVRowColumn(i), new CSVGridHeader(i));
        }
        dataGrid.setTableWidth(COLUMN_WIDTH * grid.getColumnCount(), Style.Unit.PX);
        provider.setList(grid.getRows());
    }

    @Override
    public void clearColumnHighlighting() {
        for(int i = 0; i < dataGrid.getColumnCount(); i++) {
            dataGrid.removeColumnStyleName(i, COLUMN_HIGHLIGHTING_STYLE);
        }
    }

    @Override
    public void setColumnHighlighted(int index, boolean highlighted) {
        if(highlighted) {
            dataGrid.addColumnStyleName(index, COLUMN_HIGHLIGHTING_STYLE);
        }
        else {
            dataGrid.removeColumnStyleName(index, COLUMN_HIGHLIGHTING_STYLE);
        }
    }

    @Override
    public void scrollColumnToVisible(int index) {
        final ScrollPanel scrollPanel = dataGrid.getScrollPanel();
        int horizontalPos = scrollPanel.getHorizontalScrollPosition();
        final int desiredPosition = index * COLUMN_WIDTH;
        if(horizontalPos <= desiredPosition && desiredPosition < horizontalPos + scrollPanel.getOffsetWidth()) {
            return;
        }
        scrollPanel.setHorizontalScrollPosition(desiredPosition);
    }

    @Override
    public void setColumnBold(int index, boolean bold) {
        for(int i = 0; i < dataGrid.getVisibleItemCount(); i++) {
            TableRowElement element = dataGrid.getRowElement(i);
            Element td = (Element) element.getChild(index);
            td.getStyle().setFontWeight(bold ? Style.FontWeight.BOLD : Style.FontWeight.NORMAL);
        }
    }

    public void removeColumnStyleName(String styleName) {
        for(int i = 0; i < dataGrid.getColumnCount(); i++) {
            dataGrid.removeColumnStyleName(i, styleName);
        }
    }

    public void addTDStyleName(int index, String styleName) {
        Element parent = dataGrid.getRowElement(0).getParentElement();
        Element nextSibling = parent.getNextSiblingElement();
        Element parentParent = parent.getParentElement();
        parent.removeFromParent();
        for(int i = 0; i < dataGrid.getVisibleItemCount(); i++) {
            TableRowElement element = dataGrid.getRowElement(i);
            Element td = (Element) element.getChild(index);
            ((Element) td.getChild(0)).addClassName(styleName);
        }
        if(nextSibling != null) {
            parentParent.insertBefore(parent, nextSibling);
        }
        else {
            parentParent.appendChild(parent);
        }
    }

    public void removeTDStyleName(int index, String styleName) {
        Element parent = dataGrid.getRowElement(0).getParentElement();
        Element nextSibling = parent.getNextSiblingElement();
        Element parentParent = parent.getParentElement();
        parent.removeFromParent();

        for(int i = 0; i < dataGrid.getVisibleItemCount(); i++) {
            TableRowElement element = dataGrid.getRowElement(i);
            Element td = (Element) element.getChild(index);
            ((Element) td.getChild(0)).removeClassName(styleName);
        }

        if(nextSibling != null) {
            parentParent.insertBefore(parent, nextSibling);
        }
        else {
            parentParent.appendChild(parent);
        }
    }

    @Override
    public void addColumnStyleName(int index, String styleName) {
        dataGrid.addColumnStyleName(index, styleName);
    }

    @Override
    public void removeColumnStyleName(int index, String styleName) {
        dataGrid.removeColumnStyleName(index, styleName);
    }

    private class CSVRowColumn extends TextColumn<CSVRow> {

        private int columnIndex;

        private CSVRowColumn(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public String getValue(CSVRow object) {
            if (columnIndex >= object.size()) {
                return "";
            }
            else {
                return object.getColumnValue(columnIndex);
            }
        }
    }


    private class ScrollableDataGrid<T> extends DataGrid<T> {

        private ScrollableDataGrid() {
        }

        private ScrollableDataGrid(int pageSize) {
            super(pageSize);
        }

        private ScrollableDataGrid(int pageSize, ProvidesKey<T> keyProvider) {
            super(pageSize, keyProvider);
        }

        private ScrollableDataGrid(int pageSize, Resources resources) {
            super(pageSize, resources);
        }

        private ScrollableDataGrid(int pageSize, Resources resources, ProvidesKey<T> keyProvider) {
            super(pageSize, resources, keyProvider);
        }

        private ScrollableDataGrid(int pageSize, Resources resources, ProvidesKey<T> keyProvider, Widget loadingIndicator) {
            super(pageSize, resources, keyProvider, loadingIndicator);
        }

        private ScrollableDataGrid(ProvidesKey<T> keyProvider) {
            super(keyProvider);
        }

        public ScrollPanel getScrollPanel() {
            HeaderPanel header = (HeaderPanel) getWidget();
            return (ScrollPanel) header.getContentWidget();
        }
    }

    private class CSVGridHeader extends Header<String> {

        private int columnIndex;

        private Optional<String> overridingText = Optional.absent();

        private CSVGridHeader(int columnIndex) {
            super(new TextCell());
            this.columnIndex = columnIndex;
        }

        public void setOverridingText(String overridingText) {
            this.overridingText = Optional.of(overridingText);
        }

        public void clearOverridingText() {
            this.overridingText = Optional.absent();
        }

        @Override
        public String getValue() {
            if (overridingText.isPresent()) {
                return overridingText.get();
            }
            else {
                return "Column " + (columnIndex + 1);
            }
        }

        @Override
        public void render(Cell.Context context, SafeHtmlBuilder sb) {
            String value = getValue();
            sb.appendHtmlConstant(value);
        }
    }
}
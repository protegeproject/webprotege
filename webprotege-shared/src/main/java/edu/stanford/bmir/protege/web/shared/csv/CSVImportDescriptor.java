package edu.stanford.bmir.protege.web.shared.csv;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVImportDescriptor implements IsSerializable {

    private CSVRowImportType rowImportType;

    private int displayNameColumnIndex;

    private List<CSVColumnDescriptor> columnDescriptors;

    /**
     * For serialization purposes only
     */
    private CSVImportDescriptor() {
    }

    public CSVImportDescriptor(CSVRowImportType rowImportType, int displayNameColumnIndex, List<CSVColumnDescriptor> columnDescriptors) {
        this.rowImportType = checkNotNull(rowImportType);
        this.displayNameColumnIndex = checkNotNull(displayNameColumnIndex);
        this.columnDescriptors = checkNotNull(columnDescriptors);
    }

    public CSVRowImportType getRowImportType() {
        return rowImportType;
    }

    public int getDisplayNameColumnIndex() {
        return displayNameColumnIndex;
    }

    public List<CSVColumnDescriptor> getColumnDescriptors() {
        return columnDescriptors;
    }
}

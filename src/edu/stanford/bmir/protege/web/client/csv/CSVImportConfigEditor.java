package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.resources.client.CssResource;
import edu.stanford.bmir.protege.web.shared.csv.CSVColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public interface CSVImportConfigEditor {

    void setGrid(CSVGrid csvGrid);

    CSVGrid getCSVGrid();

    Optional<CSVColumnDescriptor> getColumnImportDescriptor(int columnIndex);
}

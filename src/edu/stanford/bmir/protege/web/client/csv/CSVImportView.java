package edu.stanford.bmir.protege.web.client.csv;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2013
 */
public interface CSVImportView extends IsWidget {

    void setCSVGrid(CSVGrid grid);

    Optional<CSVImportDescriptor> getImportDescriptor();
}

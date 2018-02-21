package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.shared.csv.CSVRow;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public interface CSVReaderHandler {

    void handleRow(CSVRow csvRow);
}

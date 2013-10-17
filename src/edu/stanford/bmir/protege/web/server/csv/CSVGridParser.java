package edu.stanford.bmir.protege.web.server.csv;

import edu.stanford.bmir.protege.web.shared.csv.CSVGrid;

import java.io.IOException;
import java.io.Reader;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public class CSVGridParser {



    /**
     * Reads a CSVGrid from the specified reader.
     * @return The grid that was read.  Not {@code null}.  May be empty.
     * @throws IOException If there was a problem reading from the specified reader.
     */
    public CSVGrid readAll(Reader reader) throws IOException {
        return readToLimit(reader, Integer.MAX_VALUE);
    }

    /**
     * Reads a maximum of n rows from a csv file
     * @param reader The reader that reads the file.
     * @param limit The maximum number of rows to read
     * @return The grid containing the read rows.
     * @throws IOException
     */
    public CSVGrid readToLimit(Reader reader, int limit) throws IOException {
        final CSVGrid.Builder builder = CSVGrid.builder();
        au.com.bytecode.opencsv.CSVReader r = new au.com.bytecode.opencsv.CSVReader(reader);
        for(int i = 0; i < limit; i++) {
            String [] row = r.readNext();
            if(row != null) {
                builder.addRow(row);
            }
            else {
                break;
            }
        }
        return builder.build();
    }

}

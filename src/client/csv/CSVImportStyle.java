package edu.stanford.bmir.protege.web.client.csv;

import com.google.gwt.resources.client.CssResource;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2013
 */
public interface CSVImportStyle extends CssResource {

    String displayNameColumn();

    String highlightedColumn();

    String importedColumn();

    String classIcon();

    String namedIndividualIcon();

    String numberIcon();

    String literalIcon();
}

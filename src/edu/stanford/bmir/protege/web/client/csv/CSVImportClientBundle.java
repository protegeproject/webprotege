package edu.stanford.bmir.protege.web.client.csv;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/05/2013
 */
public interface CSVImportClientBundle extends ClientBundle {

    public static final CSVImportClientBundle INSTANCE = GWT.create(CSVImportClientBundle.class);

    @Source("CSVImportStyle.css")
    CSVImportStyle csvImportStyle();




}

package edu.stanford.bmir.protege.web.client.csv;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2013
 */
public interface CSVGridResources extends DataGrid.Resources {

    public static final CSVGridResources INSTANCE = GWT.create(CSVGridResources.class);


    @Override
    @Source({DataGrid.Style.DEFAULT_CSS, "CSVGridResource.css"})
    DataGrid.Style dataGridStyle();
}

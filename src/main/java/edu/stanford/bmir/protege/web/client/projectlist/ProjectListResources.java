package edu.stanford.bmir.protege.web.client.projectlist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2013
 */
public interface ProjectListResources extends DataGrid.Resources {

    public static final ProjectListResources INSTANCE = GWT.create(ProjectListResources.class);


    @Override
    @Source({DataGrid.Style.DEFAULT_CSS, "WebProtegeDataGrid.css"})
    DataGrid.Style dataGridStyle();
}

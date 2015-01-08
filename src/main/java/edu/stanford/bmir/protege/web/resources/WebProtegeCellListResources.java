package edu.stanford.bmir.protege.web.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public interface WebProtegeCellListResources extends CellList.Resources {

    public static WebProtegeCellListResources INSTANCE = GWT.create(WebProtegeCellListResources.class);

    @Override
    @Source("webprotege-cell-list.css")
    CellList.Style cellListStyle();
}

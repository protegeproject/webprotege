package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.ui.library.common.WebProtegeDisplay;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 * <p>
 *     A (list/grid) display for projects.
 * </p>
 */
public interface ProjectListDisplay extends WebProtegeDisplay, HasSelectionHandlers<ProjectId> {

    Widget getDisplayWidget();

    void clearFilters();

    void addFilter(ProjectListDisplayFilter filter);

    void removeFilter(ProjectListDisplayFilter filter);


}

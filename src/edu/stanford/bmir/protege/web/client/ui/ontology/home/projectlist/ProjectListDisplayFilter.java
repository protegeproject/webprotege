package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist;

import edu.stanford.bmir.protege.web.client.rpc.data.ProjectData;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public interface ProjectListDisplayFilter {

    boolean isFiltered(ProjectData projectData);
}

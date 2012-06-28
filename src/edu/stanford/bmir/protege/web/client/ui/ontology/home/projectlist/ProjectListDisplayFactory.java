package edu.stanford.bmir.protege.web.client.ui.ontology.home.projectlist;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public class ProjectListDisplayFactory {

    public static ProjectListDisplay createProjectListDisplay() {
        return new ProjectListDisplayImpl();
    }
}

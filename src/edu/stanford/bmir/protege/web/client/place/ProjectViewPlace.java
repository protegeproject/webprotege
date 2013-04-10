package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/04/2013
 */
public class ProjectViewPlace extends Place {

    private ProjectId projectId;

    public ProjectViewPlace(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return "ProjectViewPlace".hashCode() + projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectViewPlace)) {
            return false;
        }
        ProjectViewPlace other = (ProjectViewPlace) obj;
        return this.projectId.equals(other.projectId);
    }



    @Prefix("Edit")
    public static class Tokenizer implements PlaceTokenizer<ProjectViewPlace> {

        private static final String PROJECT_ID_VAR = "projectId=";

        @Override
        public ProjectViewPlace getPlace(String token) {
            return new ProjectViewPlace(ProjectId.get(token.substring(PROJECT_ID_VAR.length())));
        }

        @Override
        public String getToken(ProjectViewPlace place) {
            return PROJECT_ID_VAR + place.getProjectId().getProjectName();
        }
    }



}

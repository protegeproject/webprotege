package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/04/2013
 */
public class ProjectListPlace extends Place {

    private static final String DEFAULT_COLLECTION = "Home";

    public static final ProjectListPlace DEFAULT_PLACE = new ProjectListPlace(DEFAULT_COLLECTION, Optional.<ProjectId>absent());

    private Optional<ProjectId> selectedProject;

    private String collection;


    public ProjectListPlace(String collection, Optional<ProjectId> selectedProject) {
        this.collection = checkNotNull(collection);
        this.selectedProject = checkNotNull(selectedProject);
    }

    public Optional<ProjectId> getSelectedProject() {
        return selectedProject;
    }

    public String getCollection() {
        return collection;
    }

    @Override
    public int hashCode() {
        return "ProjectListPlace".hashCode() + collection.hashCode() + selectedProject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectListPlace)) {
            return false;
        }
        ProjectListPlace other = (ProjectListPlace) obj;
        return this.collection.equals(other.collection) && this.selectedProject.equals(other.selectedProject);
    }



    @Prefix("List")
    public static class Tokenizer implements PlaceTokenizer<ProjectListPlace> {

        public static final String COLLECTION_PROPERTY = "coll";

        private static final String SEL_PROJECT_ID_PROPERTY = "projectId";

        @Override
        public ProjectListPlace getPlace(String token) {
            PlacePropertyValueList props = PlacePropertyValueList.parse(token);
            String coll = props.getPropertyValue(COLLECTION_PROPERTY, DEFAULT_COLLECTION);
            String sel = props.getPropertyValue(SEL_PROJECT_ID_PROPERTY, null);
            return new ProjectListPlace(coll, ProjectId.getFromNullable(sel));
        }

        @Override
        public String getToken(ProjectListPlace place) {
            PlacePropertyValueList.Builder builder = PlacePropertyValueList.builder();
            builder.set(COLLECTION_PROPERTY, place.getCollection());
            if(place.getSelectedProject().isPresent()) {
                builder.set(SEL_PROJECT_ID_PROPERTY, place.getSelectedProject().get().getProjectName());
            }
            return builder.build().render();
//            return COLLECTION_PROPERTY + place.getCollection() + (place.getSelectedProject().isPresent() ? ";" + "projectId=" + place.getSelectedProject() : "");
        }
    }
}

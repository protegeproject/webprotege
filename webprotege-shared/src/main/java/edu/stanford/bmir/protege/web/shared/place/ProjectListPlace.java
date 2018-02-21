package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/04/2013
 */
public class ProjectListPlace extends Place {

    public ProjectListPlace() {
    }

    @Override
    public int hashCode() {
        return "ProjectListPlace".hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        return obj instanceof ProjectListPlace;
    }



//    @Prefix("List")
//    public static class Tokenizer implements PlaceTokenizer<ProjectListPlace> {
//
//        public static final String COLLECTION_PROPERTY = "coll";
//
//        private static final String SEL_PROJECT_ID_PROPERTY = "projectId";
//
//        @Override
//        public ProjectListPlace getPlace(String token) {
//            PlacePropertyValueList props = PlacePropertyValueList.parse(token);
//            String coll = props.getPropertyValue(COLLECTION_PROPERTY, DEFAULT_COLLECTION);
//            String sel = props.getPropertyValue(SEL_PROJECT_ID_PROPERTY, null);
//            return new ProjectListPlace(coll, ProjectId.getFromNullable(sel));
//        }
//
//        @Override
//        public String getToken(ProjectListPlace place) {
//            PlacePropertyValueList.Builder builder = PlacePropertyValueList.builder();
//            builder.set(COLLECTION_PROPERTY, place.getCollection());
//            if(place.getSelectedProject().isPresent()) {
//                builder.set(SEL_PROJECT_ID_PROPERTY, place.getSelectedProject().get().getId());
//            }
//            return builder.build().render();
////            return COLLECTION_PROPERTY + place.getCollection() + (place.getSelectedProject().isPresent() ? ";" + "projectId=" + place.getSelectedProject() : "");
//        }
//    }
}

package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Optional;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.HashSet;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
public class WebProtegePlaceTokenizer  {
//
//
//    private static final String PROJECT_ID_VAR_NAME = "projectId";
//
//    @Override
//    public String getToken(WebProtegePlace place) {
//        StringBuilder sb = new StringBuilder();
//        Optional<ProjectId> id = place.getProjectId();
//        if(id.isPresent()) {
//            sb.append(PROJECT_ID_VAR_NAME);
//            sb.append("=");
//            sb.append(id.get().getDisplayName());
//            sb.append(";");
//            for(PlaceCoordinate coordinate : place.getCoordinates()) {
//                sb.append(coordinate.getName());
//                sb.append("=");
//                sb.append(coordinate.getValue());
//                sb.append(";");
//            }
//        }
//        return sb.toString();
//    }
//
//
//    @Override
//    public WebProtegePlace getPlace(String token) {
//        Optional<ProjectId> projectId = Optional.absent();
//        RegExp regExp = RegExp.compile("([^=]+)=([^;]+);", "g");
//        MatchResult result = regExp.exec(token);
//        Set<PlaceCoordinate> coords = new HashSet<PlaceCoordinate>();
//        while(result != null) {
//            String name = result.getGroup(1);
//            String value = result.getGroup(2);
//            if(PROJECT_ID_VAR_NAME.equals(name)) {
//                projectId = Optional.of(ProjectId.get(value));
//            }
//            else {
//                PlaceCoordinate placeCoordinate = new PlaceCoordinate(name, value);
//                coords.add(placeCoordinate);
//            }
//            final int nextIndex = result.getIndex() + result.getGroup(0).length();
//            regExp.setLastIndex(nextIndex);
//            result = regExp.exec(token);
//        }
//        return new WebProtegePlace(projectId, coords);
//    }

}

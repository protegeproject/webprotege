package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
@WithTokenizers({ProjectListPlace.Tokenizer.class, ProjectViewPlace.Tokenizer.class})
public interface WebProtegePlaceHistoryMapper extends PlaceHistoryMapper {


}

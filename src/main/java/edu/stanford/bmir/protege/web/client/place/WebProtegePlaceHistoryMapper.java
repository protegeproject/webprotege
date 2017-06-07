package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsPlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSettingsPlaceTokenizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/04/2013
 */
@WithTokenizers(
        {
                ProjectListPlaceTokenizer.class,
                ProjectViewPlaceTokenizer.class,
                LoginPlaceTokenizer.class,
                SignUpPlaceTokenizer.class,
                SharingSettingsPlaceTokenizer.class,
                AdminPlaceTokenizer.class
        })
public class WebProtegePlaceHistoryMapper implements PlaceHistoryMapper {

    private List<WebProtegePlaceTokenizer> tokenizers = new ArrayList<>();

    public WebProtegePlaceHistoryMapper() {
        tokenizers.add(new ProjectListPlaceTokenizer());
        tokenizers.add(new ProjectViewPlaceTokenizer());
        tokenizers.add(new LoginPlaceTokenizer());
        tokenizers.add(new SignUpPlaceTokenizer());
        tokenizers.add(new SharingSettingsPlaceTokenizer());
        tokenizers.add(new ProjectSettingsPlaceTokenizer());
        tokenizers.add(new AdminPlaceTokenizer());
    }

    @Override
    public Place getPlace(String token) {
        for(WebProtegePlaceTokenizer tokenizer : tokenizers) {
            if(tokenizer.matches(token)) {
                return tokenizer.getPlace(token);
            }
        }
        return null;
    }

    @Override
    public String getToken(Place place) {
        for(WebProtegePlaceTokenizer tokenizer : tokenizers) {
            if(tokenizer.getPlaceClass().getName().equals(place.getClass().getName())) {
                return tokenizer.getToken(place);
            }
        }
        return null;
    }
}

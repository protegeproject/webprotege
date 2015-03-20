package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/04/2012
 */
public class OWLAPISearchManager {

    private BidirectionalShortFormProvider shortFormProvider;

    private LegacyEntityDataProvider entityDataProvider;

    @Inject
    public OWLAPISearchManager(LegacyEntityDataProvider entityDataProvider, BidirectionalShortFormProvider shortFormProvider) {
        this.shortFormProvider = shortFormProvider;
        this.entityDataProvider = entityDataProvider;
    }

    public List<EntityData> search(final String search) {
        final String normalizedSearchString;
        if(search.startsWith("*") && search.endsWith("*")) {
            normalizedSearchString = search.substring(1, search.length() - 1);
        }
        else {
            normalizedSearchString = search;
        }
        List<EntityData> result = new ArrayList<EntityData>();

        // Needs to be more efficient, but will do for now
        Set<String> shortForms = shortFormProvider.getShortForms();
        Pattern pattern = Pattern.compile(normalizedSearchString, Pattern.CASE_INSENSITIVE);

        
        for(String shortForm : shortForms) {
            Matcher matcher = pattern.matcher(shortForm);
            if(matcher.find()) {
                Set<OWLEntity> entities = shortFormProvider.getEntities(shortForm);
                for(OWLEntity entity : entities) {
                    EntityData entityData = entityDataProvider.getEntityData(entity);
                    result.add(entityData);
                }
            }
        }
        Collections.sort(result, new Comparator<EntityData>() {
            @Override
            public int compare(EntityData entityData, EntityData entityData2) {
                String browserText1 = entityData.getBrowserText();
                String browserText2 = entityData2.getBrowserText();
                if (browserText1 == null) {
                    if(browserText2 == null) {
                        return 0;
                    }
                    else {
                        return 1;
                    }
                }
                else if(browserText2 == null) {
                    return -1;
                }
                if(browserText1.equalsIgnoreCase(normalizedSearchString)) {
                    return -1;
                }
                else if(browserText2.equalsIgnoreCase(normalizedSearchString)) {
                    return 1;
                }
                return browserText1.compareToIgnoreCase(browserText2);
            }
        });
        return result;
    }


}

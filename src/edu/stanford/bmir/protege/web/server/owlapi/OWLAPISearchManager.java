package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 06/04/2012
 */
public class OWLAPISearchManager {

    private OWLAPIProject project;

    public OWLAPISearchManager(OWLAPIProject project) {
        this.project = project;
    }
    
    public List<EntityData> search(String searchString) {
        if(searchString.startsWith("*") && searchString.endsWith("*")) {
            searchString = searchString.substring(1, searchString.length() - 1);
        }
        List<EntityData> result = new ArrayList<EntityData>();

        RenderingManager rm = project.getRenderingManager();
        BidirectionalShortFormProvider sfp = rm.getShortFormProvider();
        // Needs to be more efficient, but will do for now
        Set<String> shortForms = sfp.getShortForms();
        Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);

        
        for(String shortForm : shortForms) {
            Matcher matcher = pattern.matcher(shortForm);
            if(matcher.find()) {
                Set<OWLEntity> entities = sfp.getEntities(shortForm);
                for(OWLEntity entity : entities) {
                    EntityData entityData = rm.getEntityData(entity);
                    result.add(entityData);
                }
            }
        }
        return result;
    }


}

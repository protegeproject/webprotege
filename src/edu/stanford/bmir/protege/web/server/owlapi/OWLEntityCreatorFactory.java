package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public abstract class OWLEntityCreatorFactory {

    public static final String DEFAULT_BASE = "http://webprotege.stanford.edu/entities";
    
    public static final String DEFAULT_BASE_SEPARATOR = "#";

    public abstract <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(OWLAPIProject project, UserId userId, String shortName, EntityType<E> entityType);


    public static String escapeShortName(String shortName) {
        return shortName.replace(" ", "_");
    }
    
    public static String getDefaultIRIBase(OWLOntologyID id) {
        if(id.isAnonymous()) {
            return DEFAULT_BASE + DEFAULT_BASE_SEPARATOR;
        }
        else {
            String base = id.getOntologyIRI().toString();
            if(base.endsWith("/")) {
                return base;
            }
            if(base.endsWith("#")) {
                return base;
            }
            if(base.endsWith(":")) {
                return base;
            }
            return base + DEFAULT_BASE_SEPARATOR;
        }
    }

}

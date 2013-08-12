package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.user.UserId;
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

    public static final String DEFAULT_BASE = "http://webprotege.stanford.edu/";
    
    public abstract <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(OWLAPIProject project, UserId userId, String shortName, EntityType<E> entityType);

    public abstract <E extends OWLEntity> OWLEntityCreator<E> setBrowserText(OWLAPIProject project, UserId userId, E entity, String browserText);

    public static String escapeShortName(String shortName) {
        return shortName.replace(" ", "_");
    }
    
    public static String getDefaultIRIBase(OWLOntologyID id, EntityType entityType) {
        StringBuilder sb = new StringBuilder();
        sb.append(DEFAULT_BASE);
        return sb.toString();
    }
}

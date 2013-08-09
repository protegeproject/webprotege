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
//        if(id.isAnonymous()) {
        StringBuilder sb = new StringBuilder();
        sb.append(DEFAULT_BASE);
//        sb.append(getEntityTypePathElementName(entityType));
//        sb.append("/");
        return sb.toString();
    }

//    private static String getEntityTypePathElementName(EntityType entityType) {
//        if(entityType == EntityType.CLASS) {
//            return "classes";
//        }
//        else if(entityType == EntityType.OBJECT_PROPERTY) {
//            return "object_properties";
//        }
//        else if(entityType == EntityType.DATA_PROPERTY) {
//            return "data_properties";
//        }
//        else if(entityType == EntityType.ANNOTATION_PROPERTY) {
//            return "annotation_properties";
//        }
//        else if(entityType == EntityType.NAMED_INDIVIDUAL) {
//            return "named_individuals";
//        }
//        else if(entityType == EntityType.DATATYPE) {
//            return "datatypes";
//        }
//        throw new RuntimeException("Handling for entity type missing: " + entityType);
//    }

}

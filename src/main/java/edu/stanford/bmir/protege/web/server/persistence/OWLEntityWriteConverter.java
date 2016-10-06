package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.Document;
import org.semanticweb.owlapi.model.OWLEntity;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Jul 16
 */
public class OWLEntityWriteConverter implements Converter<OWLEntity, DBObject> {

    @Override
    public DBObject convert(OWLEntity entity) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("type", entity.getEntityType().getName());
        basicDBObject.put("iri", entity.getIRI().toString());
        return basicDBObject;
    }
}

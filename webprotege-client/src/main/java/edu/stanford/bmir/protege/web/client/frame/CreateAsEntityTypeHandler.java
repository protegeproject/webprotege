package edu.stanford.bmir.protege.web.client.frame;

import org.semanticweb.owlapi.model.EntityType;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public interface CreateAsEntityTypeHandler {

    void handleCreateHasEntity(String name, EntityType<?> entityType);
}

package edu.stanford.bmir.protege.web.server.legacy;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/03/15
 */
public interface LegacyEntityDataProvider {

    EntityData getEntityData(OWLEntity entity);
}

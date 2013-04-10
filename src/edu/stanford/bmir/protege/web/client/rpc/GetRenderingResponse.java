package edu.stanford.bmir.protege.web.client.rpc;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasEntityDataProvider;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/12/2012
 */
public class GetRenderingResponse implements HasEntityDataProvider, Response {

    private Map<OWLEntity, OWLEntityData> map = new HashMap<OWLEntity, OWLEntityData>();

    private GetRenderingResponse() {
    }

    public GetRenderingResponse(Map<OWLEntity, OWLEntityData> map) {
        this.map = map;
    }

    public Map<OWLEntity, OWLEntityData> getMap() {
        return map;
    }

    public Optional<OWLEntityData> getEntityData(OWLEntity entity) {
        final OWLEntityData entityData = map.get(entity);
        if(entityData == null) {
            return Optional.absent();
        }
        else {
            return Optional.of(entityData);
        }
    }

    public Optional<OWLEntityData> getRendering(IRI iri) {
        for(OWLEntity entity : map.keySet()) {
            if(entity.getIRI().equals(iri)) {
                return Optional.of(map.get(entity));
            }
        }
        return Optional.absent();
    }
}

package edu.stanford.bmir.protege.web.client.rpc;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/12/2012
 */
public class GetRendering extends ProjectSpecificAction<GetRenderingResponse> {

    private Set<OWLEntity> entities;

    private GetRendering() {
        super();
    }

    public GetRendering(ProjectId projectId, Set<? extends OWLEntity> entities) {
        super(projectId);
        this.entities = new HashSet<OWLEntity>(entities);
    }

    public GetRendering(ProjectId projectId, HasSignature hasSignature) {
        this(projectId, hasSignature.getSignature());
    }

    public GetRendering(ProjectId projectId, OWLEntity entity) {
        super(projectId);
        this.entities = new HashSet<OWLEntity>(1);
        entities.add(entity);
    }

    public Set<OWLEntity> getEntities() {
        return entities;
    }

    @Override
    public int hashCode() {
        return "GetRendering".hashCode() + getProjectId().hashCode() + entities.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetRendering)) {
            return false;
        }
        GetRendering other = (GetRendering) obj;
        return this.getProjectId().equals(other.getProjectId()) && this.entities.equals(other.entities);
    }
}

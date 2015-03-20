package edu.stanford.bmir.protege.web.shared.renderer;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/02/15
 */
public class GetEntityDataResult implements Result {

    private ImmutableMap<OWLEntity, OWLEntityData> entity2EntityDataMap;

    private GetEntityDataResult() {
    }

    public GetEntityDataResult(ImmutableMap<OWLEntity, OWLEntityData> entity2EntityDataMap) {
        this.entity2EntityDataMap = checkNotNull(entity2EntityDataMap);
    }

    public ImmutableMap<OWLEntity, OWLEntityData> getEntityDataMap() {
        return entity2EntityDataMap;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity2EntityDataMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetEntityDataResult)) {
            return false;
        }
        GetEntityDataResult other = (GetEntityDataResult) obj;
        return this.entity2EntityDataMap.equals(other.entity2EntityDataMap);
    }


    @Override
    public String toString() {
        return toStringHelper("GetEntityDataResult")
                .addValue(entity2EntityDataMap)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/12/2012
 */
public class OWLPrimitiveDataList implements Serializable, HasSignature {

    private List<OWLPrimitiveData> primitiveData = new ArrayList<OWLPrimitiveData>();

    public OWLPrimitiveDataList(List<OWLPrimitiveData> primitiveData) {
        this.primitiveData = new ArrayList<OWLPrimitiveData>(primitiveData);
    }

    private OWLPrimitiveDataList() {

    }


    public List<OWLPrimitiveData> getPrimitiveData() {
        return new ArrayList<OWLPrimitiveData>(primitiveData);
    }

    /**
     * Gets the entities that are in this list that are of the specified type.
     * @param entityType The entity type. Not {@code null}.
     * @param <T> The type
     * @return A list of entities that appear in this list that are of the specified type.  Not {@code null}.
     * @throws NullPointerException if entityType is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <T extends OWLEntity> List<T> getEntitiesOfType(EntityType<T> entityType) {
        checkNotNull(entityType);
        List<T> result = new ArrayList<T>();
        for(OWLPrimitiveData data : primitiveData) {
            if(data.getType().isEntityType()) {
                OWLEntity entity = (OWLEntity) data.getObject();
                if(entity.isType(entityType)) {
                    result.add((T) entity);
                }
            }
        }
        return result;
    }


    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        for(OWLPrimitiveData data : primitiveData) {
            if(data instanceof OWLEntityData) {
                result.add(((OWLEntityData) data).getEntity());
            }
        }
        return result;
    }
}

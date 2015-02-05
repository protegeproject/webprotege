package edu.stanford.bmir.protege.web.shared.object;

import com.google.common.base.Optional;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import java.util.Comparator;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 04/02/15
 */
public abstract class OWLEntitySelector<T extends OWLObject, E extends OWLEntity> implements OWLObjectSelector<T> {

    private EntityType<E> entityType;

    private Comparator<? super E> entityComparator;

    public OWLEntitySelector(EntityType<E> entityType, Comparator<? super E> entityComparator) {
        this.entityType = entityType;
        this.entityComparator = entityComparator;
    }

    @Override
    public Optional<T> selectOne(Iterable<T> objects) {
        Optional<E> result = Optional.absent();
        for (OWLObject obj : objects) {
            if (obj instanceof OWLEntity) {
                OWLEntity entity = (OWLEntity) obj;
                if(entity.getEntityType() == entityType) {
                    E currentEntity = (E) entity;
                    if (result.isPresent()) {
                        if (entityComparator.compare(currentEntity, result.get()) < 0) {
                            result = Optional.of(currentEntity);
                        }
                    } else {

                        result = Optional.of(currentEntity);
                    }
                }

            }
        }
        if (result.isPresent()) {
            return Optional.of(toType(result.get()));
        }
        else {
            return Optional.absent();
        }
    }

    protected abstract T toType(E entity);
}

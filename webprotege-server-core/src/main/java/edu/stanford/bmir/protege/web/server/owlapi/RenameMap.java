package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 * <p>
 *     An object that records renamings of IRIs.
 * </p>
 */
public class RenameMap {

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final Map<IRI, IRI> map = new HashMap<>();

    /**
     * Constructs a {@link RenameMap} from the specified IRI map.
     * @param map The map from which to construct this rename map.  Not {@code null}.
     * @param dataFactory
     * @param renderingManager
     * @throws NullPointerException if {@code map} is {@code null}.
     */
    @AutoFactory
    public RenameMap(@Nonnull Map<IRI, IRI> map,
                     @Provided @Nonnull OWLDataFactory dataFactory,
                     @Provided @Nonnull RenderingManager renderingManager) {
        this.dataFactory = dataFactory;
        this.renderingManager = checkNotNull(renderingManager);
        this.map.putAll(checkNotNull(map));
    }

    /**
     * Gets renamed version of the specified entity.
     * @param entity The entity.
     * @param <E> The entity type.
     * @return An entity of the same type that is a renaming for the specified entity, or the specified entity if
     * no renaming exists within this map.  Not {@code null}.
     */
    @SuppressWarnings("unchecked")
    public <E extends OWLEntityData> E getRenamedEntity(E entity) {
        IRI renamedIRI = map.get(entity.getEntity().getIRI());
        if(renamedIRI == null) {
            return entity;
        }
        OWLEntity renamedEntity = DataFactory.getOWLEntity(entity.getEntity().getEntityType(), renamedIRI);
        return (E) renderingManager.getRendering(renamedEntity);
    }

    public <E extends OWLEntity> E getRenamedEntity(E entity) {
        IRI renamedIRI = map.get(entity.getIRI());
        if(renamedIRI == null) {
            return entity;
        }
        return (E) dataFactory.getOWLEntity(entity.getEntityType(), renamedIRI);
    }

    /**
     * Gets a set containing renamed versions of the entities in the specified set.
     * @param entities The set of entities.  Not {@code null}.
     * @param <E> The entity type.
     * @return A set of entities (of the same cardinality as the specified set) that contains renamed versions of the
     * entities in the specfied set.  Not {@code null}.
     */
    public <E extends OWLEntityData>Set<E> getRenamedEntityData(Set<E> entities) {
        Set<E> result = new HashSet<>(entities.size());
        for(E entity : entities) {
            result.add(getRenamedEntity(entity));
        }
        return result;
    }

    public <E extends OWLEntity> Set<E> getRenamedEntities(Set<E> entities) {
        Set<E> result = new HashSet<>(entities.size());
        for(E entity : entities) {
            result.add(getRenamedEntity(entity));
        }
        return result;
    }
}

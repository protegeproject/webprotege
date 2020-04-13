package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAxiomsSignatureIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectAxiomsSignatureIndex;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLEntityCollectionContainerCollector;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@ProjectSingleton
public class AxiomsByEntityReferenceIndexImpl implements AxiomsByEntityReferenceIndex, OntologyAxiomsSignatureIndex, ProjectAxiomsSignatureIndex, UpdatableIndex {


    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    private final AxiomChangeHandler axiomChangeHandler = new AxiomChangeHandler();

    private final SignatureBufferSet signatureBuffer = new SignatureBufferSet();

    private final OWLEntityCollectionContainerCollector signatureCollector = new OWLEntityCollectionContainerCollector(
            signatureBuffer);

    private final Multimap<ClassKey, OWLAxiom> byClass = MultimapBuilder.hashKeys(1000)
                                                                        .hashSetValues(2)
                                                                        .build();

    private final Multimap<ObjectPropertyKey, OWLAxiom> byObjectProperty = MultimapBuilder.hashKeys(20)
                                                                                          .hashSetValues()
                                                                                          .build();

    private final Multimap<DataPropertyKey, OWLAxiom> byDataProperty = MultimapBuilder.hashKeys(20)
                                                                                      .hashSetValues()
                                                                                      .build();

    private final Multimap<AnnotationPropertyKey, OWLAxiom> byAnnotationProperty = MultimapBuilder.hashKeys(20)
                                                                                                  .hashSetValues()
                                                                                                  .build();

    private final Multimap<NamedIndividualKey, OWLAxiom> byIndividual = MultimapBuilder.hashKeys(10)
                                                                                       .hashSetValues()
                                                                                       .build();

    private final Multimap<DatatypeKey, OWLAxiom> byDatatype = MultimapBuilder.hashKeys()
            .hashSetValues()
            .build();

    private final AxiomsByReferenceVisitor referenceVisitor = new AxiomsByReferenceVisitor();

    private final ReferenceCheckVisitor referenceCheckVisitor = new ReferenceCheckVisitor();

    private final IndexingVisitor indexingVisitor = new IndexingVisitor();

    private final DeindexingVisitor deindexingVisitor = new DeindexingVisitor();

    private final OWLEntityProvider entityProvider;


    @Inject
    public AxiomsByEntityReferenceIndexImpl(OWLEntityProvider entityProvider) {
        this.entityProvider = checkNotNull(entityProvider);
        axiomChangeHandler.setAddAxiomChangeConsumer(this::handleAddAxiom);
        axiomChangeHandler.setRemoveAxiomChangeConsumer(this::handleRemoveAxiom);
    }

    private void handleAddAxiom(@Nonnull AddAxiomChange change) {
        OWLAxiom axiom = processSignature(change);
        // Process the signature
        signatureBuffer.forEach(entity -> {
            indexingVisitor.setOntologyId(change.getOntologyId());
            indexingVisitor.setAxiom(axiom);
            entity.accept(indexingVisitor);
        });
    }

    private void handleRemoveAxiom(@Nonnull RemoveAxiomChange change) {
        var axiom = processSignature(change);
        // Process the signature
        signatureBuffer.forEach(entity -> {
            deindexingVisitor.setOntologyId(change.getOntologyId());
            deindexingVisitor.setAxiom(axiom);
            entity.accept(deindexingVisitor);
        });
    }

    private OWLAxiom processSignature(@Nonnull AxiomChange change) {
        var axiom = change.getAxiom();
        // Get the axiom signature
        signatureBuffer.clear();
        axiom.accept(signatureCollector);
        return axiom;
    }

    @Override
    public boolean containsEntityInOntologyAxiomsSignature(@Nonnull OWLEntity entity,
                                                           @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(entity);
        checkNotNull(ontologyId);
        try {
            readLock.lock();
            referenceCheckVisitor.setOntologyId(ontologyId);
            return entity.accept(referenceCheckVisitor);
        } finally {
            readLock.unlock();
        }
    }

    public Stream<OWLEntity> getEntitiesInSignatureWithIri(@Nonnull IRI iri, @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(iri);
        checkNotNull(ontologyId);
        try {
            readLock.lock();
            var builder = Stream.<OWLEntity>builder();
            if(byClass.containsKey(ClassKey.get(ontologyId, iri))) {
                var cls = entityProvider.getOWLClass(iri);
                builder.add(cls);
            }
            if(byObjectProperty.containsKey(ObjectPropertyKey.get(ontologyId, iri))) {
                var property = entityProvider.getOWLObjectProperty(iri);
                builder.add(property);
            }
            if(byDataProperty.containsKey(DataPropertyKey.get(ontologyId, iri))) {
                var property = entityProvider.getOWLDataProperty(iri);
                builder.add(property);
            }
            if(byAnnotationProperty.containsKey(AnnotationPropertyKey.get(ontologyId, iri))) {
                var property = entityProvider.getOWLAnnotationProperty(iri);
                builder.add(property);
            }
            if(byIndividual.containsKey(NamedIndividualKey.get(ontologyId, iri))) {
                var individual = entityProvider.getOWLNamedIndividual(iri);
                builder.add(individual);
            }
            if(byDatatype.containsKey(DatatypeKey.get(ontologyId, iri))) {
                var datatype = entityProvider.getOWLDatatype(iri);
                builder.add(datatype);
            }
            return builder.build();
        } finally {
            readLock.unlock();
        }
    }

    @Nonnull
    @Override
    public <E extends OWLEntity> Stream<E> getProjectAxiomsSignature(@Nonnull EntityType<E> entityType) {
        return getSignatureByType(entityType, anyOntologyId(), getEntityProvider(entityType));
    }

    private Predicate<Key> anyOntologyId() {
        return key -> true;
    }

    @Nonnull
    @Override
    public <E extends OWLEntity> Stream<E> getOntologyAxiomsSignature(@Nonnull EntityType<E> type,
                                                                      @Nonnull OWLOntologyID ontologyId) {
        return getSignatureByType(type, forOntologyId(ontologyId), getEntityProvider(type));
    }

    private <E extends OWLEntity> Function<IRI, OWLEntity> getEntityProvider(EntityType<E> type) {
        if(type.equals(EntityType.CLASS)) {
            return entityProvider::getOWLClass;
        }
        else if(type.equals(EntityType.OBJECT_PROPERTY)) {
            return entityProvider::getOWLObjectProperty;
        }
        else if(type.equals(EntityType.DATA_PROPERTY)) {
            return entityProvider::getOWLDataProperty;
        }
        else if(type.equals(EntityType.ANNOTATION_PROPERTY)) {
            return entityProvider::getOWLAnnotationProperty;
        }
        else if(type.equals(EntityType.NAMED_INDIVIDUAL)) {
            return entityProvider::getOWLNamedIndividual;
        }
        else if(type.equals(EntityType.DATATYPE)) {
            return entityProvider::getOWLDatatype;
        }
        else {
            throw new RuntimeException("Unsupported Entity Type: " + type);
        }
    }

    private Predicate<Key> forOntologyId(@Nonnull OWLOntologyID ontologyId) {
        return key -> key.getOntologyId()
                         .equals(ontologyId);
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> Stream<E> getSignatureByType(@Nonnull EntityType<E> type,
                                                               Predicate<Key> filter,
                                                               Function<IRI, OWLEntity> iri2Entity) {

        readLock.lock();
        try {
            final Multimap<? extends Key, OWLAxiom> keys;
            if(type.equals(EntityType.DATATYPE)) {
                keys = byDatatype;
            }
            else if(type.equals(EntityType.CLASS)) {
                keys = byClass;
            }
            else if(type.equals(EntityType.OBJECT_PROPERTY)) {
                keys = byObjectProperty;
            }
            else if(type.equals(EntityType.DATA_PROPERTY)) {
                keys = byDataProperty;
            }
            else if(type.equals(EntityType.ANNOTATION_PROPERTY)) {
                keys = byAnnotationProperty;
            }
            else if(type.equals(EntityType.NAMED_INDIVIDUAL)) {
                keys = byIndividual;
            }
            else {
                throw new RuntimeException("Unknown Entity Type: " + type);
            }
            return (Stream<E>) ImmutableList.copyOf(keys.keySet())
                                .stream()
                                .filter(filter)
                                            .map(Key::getIri)
                                .map(iri2Entity);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Stream<OWLAxiom> getReferencingAxioms(@Nonnull OWLEntity entity,
                                                 @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(entity);
        checkNotNull(ontologyId);
        readLock.lock();
        try {
            referenceVisitor.setOntologyId(ontologyId);
            var axioms = entity.accept(referenceVisitor);
            return ImmutableList.copyOf(axioms)
                                .stream();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        try {
            writeLock.lock();
            axiomChangeHandler.handleOntologyChanges(changes);
        } finally {
            writeLock.unlock();
        }
    }


    protected interface Key {

        @Nonnull
        OWLOntologyID getOntologyId();

        @Nonnull
        IRI getIri();
    }

    @AutoValue
    public static abstract class ClassKey implements Key {

        public static ClassKey get(@Nonnull OWLOntologyID ontologyId,
                                   @Nonnull OWLClass cls) {
            return get(ontologyId, cls.getIRI());
        }

        public static ClassKey get(@Nonnull OWLOntologyID ontologyId,
                                   @Nonnull IRI clsIri) {
            return new AutoValue_AxiomsByEntityReferenceIndexImpl_ClassKey(ontologyId, clsIri);
        }
    }

    @AutoValue
    public static abstract class ObjectPropertyKey implements Key {

        public static ObjectPropertyKey get(OWLOntologyID ontologyId, OWLObjectProperty property) {
            return get(ontologyId, property.getIRI());
        }

        public static ObjectPropertyKey get(OWLOntologyID ontologyId, IRI propertyIri) {
            return new AutoValue_AxiomsByEntityReferenceIndexImpl_ObjectPropertyKey(ontologyId, propertyIri);
        }
    }

    @AutoValue
    public static abstract class DataPropertyKey implements Key {

        public static DataPropertyKey get(OWLOntologyID ontologyId, OWLDataProperty property) {
            return get(ontologyId, property.getIRI());
        }

        public static DataPropertyKey get(OWLOntologyID ontologyId, IRI propertyIri) {
            return new AutoValue_AxiomsByEntityReferenceIndexImpl_DataPropertyKey(ontologyId, propertyIri);
        }
    }

    @AutoValue
    public static abstract class AnnotationPropertyKey implements Key {

        public static AnnotationPropertyKey get(OWLOntologyID ontologyId, OWLAnnotationProperty property) {
            return get(ontologyId, property.getIRI());
        }

        public static AnnotationPropertyKey get(OWLOntologyID ontologyId, IRI propertyIri) {
            return new AutoValue_AxiomsByEntityReferenceIndexImpl_AnnotationPropertyKey(ontologyId, propertyIri);
        }
    }

    @AutoValue
    public static abstract class NamedIndividualKey implements Key {

        public static NamedIndividualKey get(OWLOntologyID ontologyId, OWLNamedIndividual individual) {
            return get(ontologyId, individual.getIRI());
        }

        public static NamedIndividualKey get(OWLOntologyID ontologyId, IRI individualIri) {
            return new AutoValue_AxiomsByEntityReferenceIndexImpl_NamedIndividualKey(ontologyId, individualIri);
        }
    }

    @AutoValue
    public static abstract class DatatypeKey implements Key {
        public static DatatypeKey get(OWLOntologyID ontologyId, OWLDatatype datatype) {
            return get(ontologyId, datatype.getIRI());
        }

        public static DatatypeKey get(OWLOntologyID ontologyId, IRI individualIri) {
            return new AutoValue_AxiomsByEntityReferenceIndexImpl_DatatypeKey(ontologyId, individualIri);
        }
    }

    private class IndexingVisitor implements OWLEntityVisitor {

        private OWLOntologyID ontologyId;

        private OWLAxiom axiom;

        public void setOntologyId(OWLOntologyID ontologyId) {
            this.ontologyId = checkNotNull(ontologyId);
        }

        public void setAxiom(OWLAxiom axiom) {
            this.axiom = checkNotNull(axiom);
        }

        @Override
        public void visit(@Nonnull OWLClass cls) {
            byClass.put(ClassKey.get(ontologyId, cls), axiom);
        }

        @Override
        public void visit(@Nonnull OWLObjectProperty property) {
            byObjectProperty.put(ObjectPropertyKey.get(ontologyId, property), axiom);
        }

        @Override
        public void visit(@Nonnull OWLDataProperty property) {
            byDataProperty.put(DataPropertyKey.get(ontologyId, property), axiom);
        }

        @Override
        public void visit(@Nonnull OWLNamedIndividual individual) {
            byIndividual.put(NamedIndividualKey.get(ontologyId, individual), axiom);
        }

        @Override
        public void visit(@Nonnull OWLDatatype datatype) {
            byDatatype.put(DatatypeKey.get(ontologyId, datatype), axiom);
        }

        @Override
        public void visit(@Nonnull OWLAnnotationProperty property) {
            byAnnotationProperty.put(AnnotationPropertyKey.get(ontologyId, property), axiom);
        }
    }

    private class DeindexingVisitor implements OWLEntityVisitor {

        private OWLOntologyID ontologyId;

        private OWLAxiom axiom;

        public void setOntologyId(OWLOntologyID ontologyId) {
            this.ontologyId = checkNotNull(ontologyId);
        }

        public void setAxiom(OWLAxiom axiom) {
            this.axiom = checkNotNull(axiom);
        }

        @Override
        public void visit(@Nonnull OWLClass cls) {
            byClass.remove(ClassKey.get(ontologyId, cls), axiom);
        }

        @Override
        public void visit(@Nonnull OWLObjectProperty property) {
            byObjectProperty.remove(ObjectPropertyKey.get(ontologyId, property), axiom);
        }

        @Override
        public void visit(@Nonnull OWLDataProperty property) {
            byDataProperty.remove(DataPropertyKey.get(ontologyId, property), axiom);
        }

        @Override
        public void visit(@Nonnull OWLNamedIndividual individual) {
            byIndividual.remove(NamedIndividualKey.get(ontologyId, individual), axiom);
        }

        @Override
        public void visit(@Nonnull OWLDatatype datatype) {
            byDatatype.remove(DatatypeKey.get(ontologyId, datatype), axiom);
        }

        @Override
        public void visit(@Nonnull OWLAnnotationProperty property) {
            byAnnotationProperty.remove(AnnotationPropertyKey.get(ontologyId, property), axiom);
        }
    }


    private class AxiomsByReferenceVisitor implements OWLEntityVisitorEx<Collection<OWLAxiom>> {

        private OWLOntologyID ontologyId = new OWLOntologyID();

        public void setOntologyId(@Nonnull OWLOntologyID ontologyId) {
            this.ontologyId = checkNotNull(ontologyId);
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLClass cls) {
            return byClass.get(ClassKey.get(ontologyId, cls));
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLObjectProperty property) {
            return byObjectProperty.get(ObjectPropertyKey.get(ontologyId, property));
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLDataProperty property) {
            return byDataProperty.get(DataPropertyKey.get(ontologyId, property));
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLNamedIndividual individual) {
            return byIndividual.get(NamedIndividualKey.get(ontologyId, individual));
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLDatatype datatype) {
            return byDatatype.get(DatatypeKey.get(ontologyId, datatype));
        }

        @Nonnull
        @Override
        public Collection<OWLAxiom> visit(@Nonnull OWLAnnotationProperty property) {
            return byAnnotationProperty.get(AnnotationPropertyKey.get(ontologyId, property));
        }
    }

    private class ReferenceCheckVisitor implements OWLEntityVisitorEx<Boolean> {

        private OWLOntologyID ontologyId = new OWLOntologyID();

        public void setOntologyId(@Nonnull OWLOntologyID ontologyId) {
            this.ontologyId = checkNotNull(ontologyId);
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLClass cls) {
            return byClass.containsKey(ClassKey.get(ontologyId, cls));
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLObjectProperty property) {
            return byObjectProperty.containsKey(ObjectPropertyKey.get(ontologyId, property));
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLDataProperty property) {
            return byDataProperty.containsKey(DataPropertyKey.get(ontologyId, property));
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLNamedIndividual individual) {
            return byIndividual.containsKey(NamedIndividualKey.get(ontologyId, individual));
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLDatatype datatype) {
            return byDatatype.containsKey(DatatypeKey.get(ontologyId, datatype));
        }

        @Nonnull
        @Override
        public Boolean visit(@Nonnull OWLAnnotationProperty property) {
            return byAnnotationProperty.containsKey(AnnotationPropertyKey.get(ontologyId, property));
        }
    }


    private class SignatureBufferSet implements Set<OWLEntity> {

        private final List<OWLEntity> buffer = new ArrayList<>();

        @Override
        public int size() {
            return buffer.size();
        }

        @Override
        public boolean isEmpty() {
            return buffer.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return buffer.contains(o);
        }

        @Nonnull
        @Override
        public Iterator<OWLEntity> iterator() {
            return buffer.iterator();
        }

        @Nonnull
        @Override
        public Object[] toArray() {
            return buffer.toArray();
        }

        @Nonnull
        @Override
        public <T> T[] toArray(@Nonnull T[] a) {
            return buffer.toArray(a);
        }

        @Override
        public boolean add(OWLEntity entity) {
            return buffer.add(entity);
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@Nonnull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@Nonnull Collection<? extends OWLEntity> c) {
            return false;
        }

        @Override
        public boolean retainAll(@Nonnull Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(@Nonnull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {
            buffer.clear();
        }
    }

}

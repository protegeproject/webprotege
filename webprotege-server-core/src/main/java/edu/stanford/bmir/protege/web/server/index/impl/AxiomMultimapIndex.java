package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.server.change.AxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-07
 */
public class AxiomMultimapIndex<V, A extends OWLAxiom> {

    private final AxiomChangeHandler axiomChangeHandler = new AxiomChangeHandler();

    @Nonnull
    private final Multimap<Key<V>, A> backingMap;

    @Nullable
    private final KeyValueExtractor<V, A> unaryKeyValueExtractor;

    @Nullable
    private final KeyValueExtractor<Iterable<V>, A> naryKeyValueExtractor;

    @Nonnull
    private final Class<A> axiomCls;

    private final Queue<ImmutableList<OntologyChange>> changeQueue = new ArrayDeque<>();

    private volatile boolean lazy = false;

    private boolean allowDuplicates = true;

    public static <V, A extends OWLAxiom> AxiomMultimapIndex<V, A> create(@Nonnull Class<A> axiomCls,
                                                                          @Nonnull KeyValueExtractor<V, A> keyValueExtractor,
                                                                          @Nonnull Multimap<Key<V>, A> backingMap) {
        return new AxiomMultimapIndex<>(axiomCls, keyValueExtractor, null, backingMap);
    }
    public static <V, A extends OWLAxiom> AxiomMultimapIndex<V, A> create(@Nonnull Class<A> axiomCls,
                                                                          @Nonnull KeyValueExtractor<V, A> keyValueExtractor) {
        var backingMap = IndexedSetMultimaps.<V, A>create();
        return new AxiomMultimapIndex<>(axiomCls, keyValueExtractor, null, backingMap);
    }

    public static <V, A extends OWLAxiom> AxiomMultimapIndex<V, A> createWithNaryKeyValueExtractor(@Nonnull Class<A> axiomCls,
                                                                                                   @Nonnull KeyValueExtractor<Iterable<V>, A> keyValueExtractor) {
        var backingMap = IndexedSetMultimaps.<V, A>create();
        return new AxiomMultimapIndex<>(axiomCls, null, keyValueExtractor, backingMap);
    }

    public static <V, A extends OWLAxiom> AxiomMultimapIndex<V, A> createWithNaryKeyValueExtractor(@Nonnull Class<A> axiomCls,
                                                                                                   @Nonnull KeyValueExtractor<Iterable<V>, A> keyValueExtractor,
                                                                                                   @Nonnull Multimap<Key<V>, A> backingMap) {
        return new AxiomMultimapIndex<>(axiomCls, null, keyValueExtractor, backingMap);
    }

    private AxiomMultimapIndex(@Nonnull Class<A> axiomCls,
                               @Nullable KeyValueExtractor<V, A> unaryKeyValueExtractor,
                               @Nullable KeyValueExtractor<Iterable<V>, A> naryKeyValueExtractor,
                               @Nonnull Multimap<Key<V>, A> backingMap) {
        this.backingMap = checkNotNull(backingMap);
        this.unaryKeyValueExtractor = unaryKeyValueExtractor;
        this.naryKeyValueExtractor = naryKeyValueExtractor;
        this.axiomCls = checkNotNull(axiomCls);
        axiomChangeHandler.setAxiomChangeConsumer(this::handleChange);
    }



    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public void setAllowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    private void handleChange(@Nonnull AxiomChange change) {
        var axiom = change.getAxiom();
        if(!axiomCls.isInstance(axiom)) {
            return;
        }
        var ax = axiomCls.cast(axiom);
        if(isNaryKeyValue()) {
            var values = getNaryKeyValueExtractor(ax);
            values.forEach(val -> {
                var key = Key.get(change.getOntologyId(), val);
                handleOntologyChange(change, key, ax);
            });
        }
        else {
            var keyValue = getUnaryKeyValueExtractor(ax);
            if(keyValue == null) {
                return;
            }
            var key = Key.get(change.getOntologyId(),
                              keyValue);
            handleOntologyChange(change, key, ax);
        }
    }

    private boolean isNaryKeyValue() {
        return naryKeyValueExtractor != null;
    }

    @Null
    private Iterable<V> getNaryKeyValueExtractor(A ax) {
        checkNotNull(naryKeyValueExtractor);
        return naryKeyValueExtractor.extractValue(ax);
    }

    private void handleOntologyChange(@Nonnull AxiomChange change, Key<V> key, A ax) {
        if(change.isAddAxiom()) {
            // Backing map may/may not be a set
            // If it is a set then we just added the key/axiom pair
            // If it is not a set, we don't want duplicates so we must
            // check to see if it contains the key/axiom pair
            var shouldAdd = allowDuplicates || backingMap instanceof SetMultimap || !backingMap.containsEntry(key, ax);
            if(shouldAdd) {
                backingMap.put(key, ax);
            }
        }
        else {
            backingMap.remove(key, ax);
        }
    }

    private V getUnaryKeyValueExtractor(A ax) {
        checkNotNull(unaryKeyValueExtractor);
        return unaryKeyValueExtractor.extractValue(ax);
    }

    public synchronized Stream<A> getAxioms(@Nonnull V value, @Nonnull OWLOntologyID ontologyId) {
        var key = Key.get(ontologyId, value);
        if(!changeQueue.isEmpty()) {
            applyQueuedChanges();
        }
        return ImmutableList.copyOf(backingMap.get(key))
                            .stream();
    }

    private void applyQueuedChanges() {
        while(!changeQueue.isEmpty()) {
            var changes = changeQueue.poll();
            axiomChangeHandler.handleOntologyChanges(changes);
        }
    }

    public synchronized void applyChanges(@Nonnull ImmutableList<OntologyChange> changes) {
        changeQueue.add(changes);
        if(!lazy) {
            applyQueuedChanges();
        }
    }

    public void dumpStats(PrintStream out) {
        Stats.dump("AnnotationAxiomsByIriReferenceIndex", backingMap, out);
    }
}

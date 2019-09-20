package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.HashMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-18
 */
public class IndexedSetMultimaps {

    public static <K, V> Multimap<Key<K>, V> create() {
        var backingMap = new HashMap<Key<K>, Collection<V>>();
        return Multimaps.newSetMultimap(backingMap, IndexedSet::new);
    }
}

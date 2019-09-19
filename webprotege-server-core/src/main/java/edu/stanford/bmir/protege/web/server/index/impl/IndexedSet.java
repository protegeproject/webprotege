package edu.stanford.bmir.protege.web.server.index.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-18
 */
public class IndexedSet<T> implements Set<T> {

    private static Logger logger = LoggerFactory.getLogger(IndexedSet.class);

    private static final int THRESHOLD_SIZE = 256;

    private Collection<T> delegate = new ArrayList<>(1);

    public IndexedSet() {
    }

    private void checkDelegate() {
        if(delegate.size() >= THRESHOLD_SIZE) {
            if(delegate instanceof List) {
                delegate = new HashSet<>(delegate);
            }
        }
        else {
            if(delegate instanceof Set) {
                delegate = new ArrayList<>(delegate);
            }
        }
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @Nonnull
    @Override
    public <T1> T1[] toArray(T1[] a) {
        return delegate.toArray(a);
    }

    @Override
    public boolean add(T t) {
        checkDelegate();
        if(delegate instanceof List && !delegate.contains(t)) {
            return delegate.add(t);
        }
        else {
            return delegate.add(t);
        }
    }

    @Override
    public boolean remove(Object o) {
        var removed = delegate.remove(o);
        checkDelegate();
        return removed;
    }

    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends T> c) {
        if(delegate instanceof List) {
            var unique = c.stream()
             .filter(element -> !delegate.contains(element))
             .collect(Collectors.toSet());
            return delegate.addAll(unique);
        }
        else {
            return delegate.addAll(c);
        }
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        var modified = delegate.removeAll(c);
        checkDelegate();
        return modified;
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        var modified = delegate.retainAll(c);
        checkDelegate();
        return modified;
    }

    @Override
    public void clear() {
        delegate.clear();
        checkDelegate();
    }
}

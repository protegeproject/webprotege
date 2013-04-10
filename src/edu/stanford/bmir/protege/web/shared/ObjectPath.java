package edu.stanford.bmir.protege.web.shared;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class ObjectPath<T> implements Serializable, HasSignature, Iterable<T> {

    private List<T> path = new ArrayList<T>();

    /**
     * Constructs an empty path.
     */
    public ObjectPath() {

    }

    /**
     * Constructs an {@link ObjectPath} from the specified list of element.
     * @param path The path.  Not {@code null}.
     * @throws NullPointerException if {@code path} is {@code null}.
     */
    public ObjectPath(List<T> path) {
        this.path = new ArrayList<T>(checkNotNull(path));
    }

    /**
     * Gets an {@link Iterator} that will iterate over the contexts of this path.
     * @return An iterator for this path.
     */
    public Iterator<T> iterator() {
        return path.iterator();
    }

    /**
     * Gets a list of the objects in this path.
     * @return A list objects in the path.  Modification of the returned list will not affect this path. Not {@code null}.
     */
    public List<T> getPath() {
        return new ArrayList<T>(path);
    }

    /**
     * Determines if this path is empty.
     * @return {@code true} if this path is empty, otherwise returns {@code false}.
     */
    public boolean isEmpty() {
        return path.isEmpty();
    }

    /**
     * Gets the number of objects in this path.
     * @return The number of objects in this path.
     */
    public int size() {
        return path.size();
    }

    /**
     * Gets the object at the specified index.
     * @param index The index.
     * @return The object at the specified index.
     * @throws IndexOutOfBoundsException if {@code index &lt; 0} or {@code index &gt;= size}.
     */
    public T get(int index) {
        return path.get(index);
    }

    /**
     * Gets the last element in this path.
     * @return The last element in this path.
     * @throws IndexOutOfBoundsException if the path is empty.
     */
    public T getLastElement() {
        if(path.isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
        return path.get(path.size() - 1);
    }

    public T getSecondToLastElement() {
        if(path.size() < 2) {
            throw new IndexOutOfBoundsException();
        }
        return path.get(path.size() - 2);
    }

    /**
     * Gets the signature that is comprised of the signature of all objects that are either instances of
     * {@link HasSignature}, or are instances of {@link OWLObject}.
     * @return A set of {@link OWLEntity} objects that make up the signature of this path.
     */
    @Override
    public Set<OWLEntity> getSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        for(Object element : path) {
            if (element instanceof HasSignature) {
                result.addAll(((HasSignature) element).getSignature());
            }
            else if(element instanceof OWLObject) {
                result.addAll(((OWLObject) element).getSignature());
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return "OWLEntityDataPath".hashCode() + path.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ObjectPath)) {
            return false;
        }
        ObjectPath other = (ObjectPath) obj;
        return this.path.equals(other.path);
    }
}

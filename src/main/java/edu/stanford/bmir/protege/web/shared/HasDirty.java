package edu.stanford.bmir.protege.web.shared;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/12/2012
 * <p>
 *     An interface to object which can have a "dirty" state.  Dirty usually means "edited".
 * </p>
 */
public interface HasDirty extends HasDirtyChangedHandlers {

    /**
     * Determines if this object is dirty.
     * @return {@code true} if the object is dirty, otherwise {@code false}.
     */
    boolean isDirty();
}

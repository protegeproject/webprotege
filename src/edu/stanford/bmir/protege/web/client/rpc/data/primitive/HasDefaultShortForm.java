package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/07/2012
 * <p>
 *     A marker interface for things that can provide a human readable default short form string representation of themselves
 * </p>
 */
public interface HasDefaultShortForm {

    /**
     * Gets the default short form for this object.
     * @return A string representing the short form of the implementer of this interface. Not null.
     */
    String getDefaultShortForm();
}

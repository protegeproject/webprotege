package edu.stanford.bmir.protege.web.shared.crud;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/09/2013
 */
public enum IRIPrefixUpdateStrategy {
    /**
     * Specifies that the system should rename entities whose IRIs had the previous prefix so that they
     * have the new prefix.  i.e. a find and replace.
     */
    FIND_AND_REPLACE,

    LEAVE_INTACT
}

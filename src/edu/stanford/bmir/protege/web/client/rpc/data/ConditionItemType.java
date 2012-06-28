package edu.stanford.bmir.protege.web.client.rpc.data;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 *
 * <p>
 *     A hack to get around the fact that this info is not represented in condition items - without it, it's really
 *     difficult to parse condition items into axioms (things that don't exist in P3 I suppose).
 * </p>
 */
public enum ConditionItemType {

    NECESSARY,

    NECESSARY_AND_SUFFICIENT,

    /**
     * Indicates the type isn't set to be either necessary, or necessary & sufficient.  A horrible hack.
     */
    UNDEFINED
}

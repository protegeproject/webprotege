package edu.stanford.bmir.protege.web.shared.crud;


import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 * <p>
 *     The settings for an {@link edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler}. All subclasses of this class
 *     provide an IRI prefix for entity creation.  Settings specific to each concrete subclass are used to generate an
 *     IRI suffix for an entity.  This suffix may or may not depend upon a supplied short form for the entity.
 * </p>
 */
public abstract class EntityCrudKitSuffixSettings implements HasKitId, Serializable {

}

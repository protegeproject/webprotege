package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public abstract class Entity implements Primitive, HasDefaultShortForm, HasVisualObject, Serializable {

    private IRI iri;
    
    private String builtinShortForm = null;
    
    protected Entity() {
    }

    protected Entity(Prefix prefix, String localName) {
        this.iri = IRI.create(prefix + localName);
        builtinShortForm = prefix.getPrefixName() + ":" + localName;
    }
    
    /**
     * Constructs an entity with the specified IRI.
     * @param iri The IRI. Not null.
     * @throws NullPointerException if iri is null.
     */
    public Entity(IRI iri) {
        if(iri == null) {
            throw new NullPointerException("iri parameter must not be null");
        }
        this.iri = iri;
    }

    /**
     * Gets the IRI of this entity.
     * @return The IRI of this entity.  Not null.
     */
    public IRI getIRI() {
        return iri;
    }


    /**
     * Gets the default short form for this entity.  This is the default short form of the IRI of this entity.  Note
     * that this may not be the same as the preferred browser text of the visual form of this entity.
     * @return A string representing the short form of this entity.  Not null.
     * @see {@link edu.stanford.bmir.protege.web.client.rpc.data.primitive.IRI#getDefaultShortForm()}.
     */
    public String getDefaultShortForm() {
        if(builtinShortForm != null) {
            return builtinShortForm;
        }
        return iri.getDefaultShortForm();
    }

    public VisualObject<?> toVisualObject() {
        return toVisualObject(getDefaultShortForm());
    }

    public abstract <R, E extends Exception> R accept(EntityVisitor<R, E> visitor) throws E;

}

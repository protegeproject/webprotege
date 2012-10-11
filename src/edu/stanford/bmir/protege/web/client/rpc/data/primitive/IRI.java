package edu.stanford.bmir.protege.web.client.rpc.data.primitive;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 * <p>
 *     Represents an IRI - used to name entities and in annotations.
 * </p>
 */
public class IRI implements AnnotationPropertyEdgeSubject, AnnotationPropertyEdgeValue, Serializable, HasDefaultShortForm {

    private String iri;

    /**
     * A private constructor for serialization purposes only
     */
    private IRI() {
    }

    /**
     * Creates an IRI from the specified string.
     * @param iri The IRI whose string is the lexical form.  No parsing of the string is done to ensure correctness.
     * @throws NullPointerException if iri is null.
     * @return The IRI whose lexical form is the specified string.
     */
    public static IRI create(String iri) {
        return new IRI(iri);
    }

    private IRI(String iri) {
        this.iri = iri;
    }


    /**
     * A convenience method to get the short form of this IRI.
     * @return The short form, which is determined as follows:  If the IRI has a fragement then the short form is the
     * fragment.  If the IRI does not have a fragment but has a path then the short form is the last path element name
     * which is not the lexical form of a number, else the short form is the whole IRI.  Not <code>null</code>.
     */
    public String getDefaultShortForm() {
        int hashSep = iri.lastIndexOf("#");
        if(hashSep != -1) {
            return iri.substring(hashSep);
        }
        int slashSep = iri.lastIndexOf("/");
        if(slashSep != -1) {
            return iri.substring(slashSep);
        }
        return iri;
    }

    public String getIRI() {
        return iri;
    }

    @Override
    public int hashCode() {
        return iri.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof IRI)) {
            return false;
        }
        IRI other = (IRI) obj;
        return other.iri.equals(this.iri);
    }
}

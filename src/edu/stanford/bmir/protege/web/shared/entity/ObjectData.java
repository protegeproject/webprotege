package edu.stanford.bmir.protege.web.shared.entity;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasBrowserText;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 * <p>
 *     Represents data about some object.  This data includes browser text for the object.
 * </p>
 */
public abstract class ObjectData<O> implements Serializable, HasBrowserText, HasSignature, IsSerializable {

    private final O object;

    protected ObjectData(O object) {
        this.object = object;
    }

    public O getObject() {
        return object;
    }

    /**
     * Gets the signature of this object.
     * @return A (possibly empty) set representing the signature of this object.
     */
    @Override
    public Set<OWLEntity> getSignature() {
        if(object instanceof HasSignature) {
            return ((HasSignature) object).getSignature();
        }
        else {
            return Collections.emptySet();
        }
    }


}

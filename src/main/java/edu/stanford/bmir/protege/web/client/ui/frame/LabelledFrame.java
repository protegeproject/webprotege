package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasSignature;
import edu.stanford.bmir.protege.web.shared.frame.Frame;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.Serializable;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class LabelledFrame<F extends Frame<?>> implements IsSerializable, HasSignature {

    private String displayName;

    private F frame;

    protected LabelledFrame() {
    }

    public LabelledFrame(String displayName, F frame) {
        this.displayName = displayName;
        this.frame = frame;
    }

    public String getDisplayName() {
        return displayName;
    }

    public F getFrame() {
        return frame;
    }

    @Override
    public int hashCode() {
        return "LabelledClassFrame".hashCode() + displayName.hashCode() + frame.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof LabelledFrame)) {
            return false;
        }
        LabelledFrame other = (LabelledFrame) obj;
        return displayName.equals(other.displayName) && frame.equals(other.frame);
    }

    /**
     * Gets the signature of the object that implements this interface.
     * @return A set of entities that represent the signature of this object
     */
    @Override
    public Set<OWLEntity> getSignature() {
        return frame.getSignature();
    }


    @Override
    public String toString() {
        return toStringHelper("LabelledFrame")
                .addValue(displayName)
                .addValue(frame)
                .toString();
    }
}

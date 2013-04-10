package edu.stanford.bmir.protege.web.shared.genericframe;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/02/2013
 */
public class FrameSectionId {

    private String id;


    private FrameSectionId() {
    }

    public FrameSectionId(String id) {
        this.id = checkNotNull(id);
    }

    @Override
    public int hashCode() {
        return "FrameSectionId".hashCode() + id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof FrameSectionId)) {
            return false;
        }
        FrameSectionId other = (FrameSectionId) obj;
        return this.id.equals(other.id);
    }

}

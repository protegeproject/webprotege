package edu.stanford.bmir.protege.web.shared.obo;


import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermDefinition extends OBOTermMetaData implements Serializable {

    public static final OBOTermDefinition EMPTY = new OBOTermDefinition(Collections.emptyList(), "");

    private String definition;

    private OBOTermDefinition() {
        super();
    }

    public static OBOTermDefinition empty() {
        return EMPTY;
    }

    public OBOTermDefinition(List<OBOXRef> xrefs, String definition) {
        super(xrefs);
        this.definition = checkNotNull(definition);
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(definition, getXRefs());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OBOTermDefinition)) {
            return false;
        }
        OBOTermDefinition other = (OBOTermDefinition) obj;
        return this.definition.equals(other.definition)
                && this.getXRefs().equals(other.getXRefs());
    }


    @Override
    public String toString() {
        return toStringHelper("OBOTermDefinition")
                .add("definition", definition)
                .addValue(getXRefs())
                .toString();
    }
}

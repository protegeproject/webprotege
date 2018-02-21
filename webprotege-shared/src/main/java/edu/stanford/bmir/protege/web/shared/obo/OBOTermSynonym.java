package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import java.io.Serializable;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermSynonym extends OBOTermMetaData implements Serializable {

    private String name;
    
    private OBOTermSynonymScope scope;


    @GwtSerializationConstructor
    private OBOTermSynonym() {
    }

    public OBOTermSynonym(List<OBOXRef> xrefs, String name, OBOTermSynonymScope scope) {
        super(xrefs);
        this.name = checkNotNull(name);
        this.scope = checkNotNull(scope);
    }

    public String getName() {
        return name;
    }

    public OBOTermSynonymScope getScope() {
        return scope;
    }
    
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, scope);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OBOTermSynonym)) {
            return false;
        }
        OBOTermSynonym other = (OBOTermSynonym) obj;
        return this.name.equals(other.name)
                && this.scope.equals(other.scope)
                && this.getXRefs().equals(other.getXRefs());
    }


    @Override
    public String toString() {
        return toStringHelper("OBOTermSynonym")
                .addValue(scope)
                .addValue(name)
                .addValue(getXRefs())
                .toString();
    }
}

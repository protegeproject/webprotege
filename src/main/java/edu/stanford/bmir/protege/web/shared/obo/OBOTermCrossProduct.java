package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermCrossProduct implements Serializable {

    @SuppressWarnings("GwtInconsistentSerializableClass")
    @Nullable
    private OWLClassData genus;

    private OBOTermRelationships relationships;

    /**
     * For serialization purposes only
     */
    private OBOTermCrossProduct() {
    
    }
    
    public static OBOTermCrossProduct emptyOBOTermCrossProduct() {
        return new OBOTermCrossProduct(Optional.empty(), new OBOTermRelationships(Collections.emptySet()));
    }

    public OBOTermCrossProduct(Optional<OWLClassData> genus, OBOTermRelationships relationships) {
        this.genus = checkNotNull(genus).orElse(null);
        this.relationships = checkNotNull(relationships);
    }

    public Optional<OWLClassData> getGenus() {
        return Optional.ofNullable(genus);
    }

    public OBOTermRelationships getRelationships() {
        return relationships;
    }
    
    public boolean isEmpty() {
        return genus == null || relationships.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(genus, relationships);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof OBOTermCrossProduct)) {
            return false;
        }
        OBOTermCrossProduct other = (OBOTermCrossProduct) obj;
        return Objects.equal(this.genus, other.genus) && this.relationships.equals(other.relationships);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("OBOTermCrossProduct")
                .addValue(genus)
                .addValue(relationships)
                .toString();
    }
}

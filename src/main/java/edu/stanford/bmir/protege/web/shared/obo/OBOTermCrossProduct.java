package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;

import java.io.Serializable;
import java.util.Collections;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/05/2012
 */
public class OBOTermCrossProduct implements Serializable {

    private Optional<OWLClassData> genus;

    private OBOTermRelationships relationships;

    /**
     * For serialization purposes only
     */
    private OBOTermCrossProduct() {
    
    }
    
    public static OBOTermCrossProduct emptyOBOTermCrossProduct() {
        return new OBOTermCrossProduct(Optional.absent(), new OBOTermRelationships(Collections.emptySet()));
    }

    public OBOTermCrossProduct(Optional<OWLClassData> genus, OBOTermRelationships relationships) {
        this.genus = checkNotNull(genus);
        this.relationships = checkNotNull(relationships);
    }

    public Optional<OWLClassData> getGenus() {
        return genus;
    }

    public OBOTermRelationships getRelationships() {
        return relationships;
    }
    
    public boolean isEmpty() {
        return !genus.isPresent() || relationships.isEmpty();
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
        return this.genus.equals(other.genus) && this.relationships.equals(other.relationships);
    }


    @Override
    public String toString() {
        return toStringHelper("OBOTermCrossProduct")
                .addValue(genus)
                .addValue(relationships)
                .toString();
    }
}

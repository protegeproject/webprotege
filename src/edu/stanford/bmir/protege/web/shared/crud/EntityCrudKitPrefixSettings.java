package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasIRIPrefix;
import org.springframework.data.annotation.TypeAlias;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/08/2013
 */
@TypeAlias("EntityCrudKitPrefixSettings")
public final class EntityCrudKitPrefixSettings implements HasIRIPrefix, Serializable {

    private static final String DEFAULT_IRI_PREFIX = "http://webprotege.stanford.edu/";

    private String iriPrefix;

    public EntityCrudKitPrefixSettings() {
        this(DEFAULT_IRI_PREFIX);
    }

    public EntityCrudKitPrefixSettings(String iriPrefix) {
        this.iriPrefix = iriPrefix;
    }

    @Override
    public String getIRIPrefix() {
        return iriPrefix;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("EntityCrudKitPrefixSettings")
                .addValue(iriPrefix).toString();
    }
}

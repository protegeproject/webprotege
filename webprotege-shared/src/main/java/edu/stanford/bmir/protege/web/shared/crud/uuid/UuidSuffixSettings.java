package edu.stanford.bmir.protege.web.shared.crud.uuid;


import com.fasterxml.jackson.annotation.*;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitId;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings.TYPE_ID;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 * <p>
 *
 * </p>
 */
@AutoValue
@GwtCompatible(serializable = true)
@JsonTypeName(TYPE_ID)
public abstract class UuidSuffixSettings extends EntityCrudKitSuffixSettings {

    public static final String TYPE_ID = "Uuid";

    /**
     * A start char for local names.  Some UUIDs might start with a number.  Unfortunately, NCNames (non-colonised names)
     * in XML cannot start with numbers.  For everything apart from properties this is o.k. but for properties it means
     * that it might not be possible to save an ontology in RDF/XML.  We therefore prefix each local name with a valid
     * NCName start char - "R".  The character "R" was chosen so as not to encode the type into the name.  I initially
     * considered C for classes, P properties etc. however with punning this would get ugly.
     */
    public static final String BASE62_ID_PREFIX = "R";

    public static final String STARDARD_ID_PREFIX = "id-";

    protected static final String UUID_FORMAT = "uuidFormat";

    public static UuidSuffixSettings get() {
        return get(UuidFormat.BASE62);
    }

    @JsonCreator
    public static UuidSuffixSettings get(@JsonProperty(UUID_FORMAT) @Nullable UuidFormat format) {
        if(format == null) {
            return new AutoValue_UuidSuffixSettings(UuidFormat.BASE62);
        }
        else {
            return new AutoValue_UuidSuffixSettings(format);
        }
    }

    @JsonIgnore
    @Override
    public EntityCrudKitId getKitId() {
        return UuidSuffixKit.getId();
    }

    @JsonProperty(UUID_FORMAT)
    @Nonnull
    public abstract UuidFormat getUuidFormat();

    @JsonIgnore
    @Nonnull
    public String getIdPrefix() {
        if(getUuidFormat().equals(UuidFormat.BASE62)) {
            return BASE62_ID_PREFIX;
        }
        else {
            return STARDARD_ID_PREFIX;
        }
    }
}

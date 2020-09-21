package edu.stanford.bmir.protege.web.shared.sharing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/05/15
 *
 * Identifies a Person in a sharing setting.  The person may or may not be a user in webprotege.
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class PersonId implements Serializable, IsSerializable, Comparable<PersonId> {

    @JsonCreator
    @Nonnull
    public static PersonId get(@Nonnull String id) {
        return new AutoValue_PersonId(id);
    }

    @Nonnull
    public static PersonId of(UserId userId) {
        return get(userId.getUserName());
    }

    @JsonValue
    @Nonnull
    public abstract String getId();



    @Override
    public int compareTo(@Nonnull PersonId o) {
        if(this == o) {
            return 0;
        }
        int diff = this.getId().compareToIgnoreCase(o.getId());
        if(diff != 0) {
            return diff;
        }
        return this.getId().compareTo(o.getId());
    }
}

package edu.stanford.bmir.protege.web.shared.issues;

import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;

import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Oct 2016
 */
public class ThreadId implements IsSerializable {

    private String id;

    public ThreadId(@Nonnull String id) {
        this.id = checkNotNull(id);
    }

    @GwtIncompatible
    public static ThreadId create() {
        //noinspection NonJREEmulationClassesInClientCode
        return new ThreadId(UUID.randomUUID().toString());
    }

    @GwtSerializationConstructor
    private ThreadId() {
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ThreadId)) {
            return false;
        }
        ThreadId other = (ThreadId) obj;
        return this.id.equals(other.id);
    }


    @Override
    public String toString() {
        return toStringHelper("ThreadId")
                .addValue(id)
                .toString();
    }
}


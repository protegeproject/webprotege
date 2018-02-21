package edu.stanford.bmir.protege.web.shared.issues;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Aug 16
 */
public class Milestone implements IsSerializable {

    @Nonnull
    private String label;

    @GwtSerializationConstructor
    private Milestone() {
    }

    public Milestone(@Nonnull String label) {
        this.label = checkNotNull(label);
        checkArgument(!label.isEmpty(), "Milestone label must not be empty");
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Milestone)) {
            return false;
        }
        Milestone other = (Milestone) obj;
        return this.label.equals(other.label);
    }


    @Override
    public String toString() {
        return toStringHelper("Milestone")
                .addValue(label)
                .toString();
    }
}

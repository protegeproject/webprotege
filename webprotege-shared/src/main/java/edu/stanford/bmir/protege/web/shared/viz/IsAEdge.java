package edu.stanford.bmir.protege.web.shared.viz;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class IsAEdge implements Edge {

    private static final String DESCRIPTOR = "ISA";

    public static IsAEdge get(@Nonnull OWLEntityData tail,
                              @Nonnull OWLEntityData head) {
        return new AutoValue_IsAEdge(head, tail);
    }

    @Override
    public String getRelationshipDescriptor() {
        return DESCRIPTOR;
    }

    @Nonnull
    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public boolean isIsA() {
        return true;
    }

    @Override
    public boolean isRelationship() {
        return false;
    }

    @Override
    public Optional<OWLEntityData> getLabellingEntity() {
        return Optional.empty();
    }
}

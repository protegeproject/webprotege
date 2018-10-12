package edu.stanford.bmir.protege.web.server.viz;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@AutoValue
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
}

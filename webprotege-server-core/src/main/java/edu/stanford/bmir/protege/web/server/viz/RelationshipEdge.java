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
public abstract class RelationshipEdge implements Edge {

    @Nonnull
    public static RelationshipEdge get(@Nonnull OWLEntityData tail,
                                       @Nonnull OWLEntityData head,
                                       @Nonnull OWLEntityData relationship) {
        return new AutoValue_RelationshipEdge(head, tail, relationship);
    }

    @Nonnull
    public abstract OWLEntityData getRelationship();

    @Override
    public String getRelationshipDescriptor() {
        return getRelationship().getEntity().getIRI().toString();
    }

    @Nonnull
    @Override
    public String getLabel() {
        return getRelationship().getBrowserText();
    }

    @Override
    public boolean isIsA() {
        return false;
    }
}

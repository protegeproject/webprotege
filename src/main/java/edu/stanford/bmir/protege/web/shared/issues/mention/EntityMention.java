package edu.stanford.bmir.protege.web.shared.issues.mention;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.issues.Mention;
import org.semanticweb.owlapi.model.OWLEntity;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.TypeAlias;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Sep 16
 */
@Persistent
public class EntityMention extends Mention {

    @Nonnull
    private OWLEntity entity;

    @PersistenceConstructor
    public EntityMention(@Nonnull OWLEntity entity) {
        this.entity = entity;
    }

    @GwtSerializationConstructor
    private EntityMention() {
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }


    @Override
    public String toString() {
        return toStringHelper("EntityMention")
                .addValue(entity)
                .toString();
    }
}

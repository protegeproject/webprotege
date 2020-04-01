package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-03-31
 */
public abstract class PlainEntityFrame implements Frame<OWLEntity> {

    @Nonnull
    public abstract OWLEntity getSubject();

    @Nonnull
    public abstract ImmutableSet<? extends PlainPropertyValue> getPropertyValues();

    @Nonnull
    public abstract EntityFrame<? extends OWLEntityData> toEntityFrame(FrameComponentRenderer renderer);
}

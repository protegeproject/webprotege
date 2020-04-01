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
public abstract class PlainEntityFrame {

    @Nonnull
    public abstract OWLEntity getSubject();

    /**
     * Gets the {@link PropertyValue}s in this frame.
     *
     * @return The (possibly empty) set of property values in this frame. Not {@code null}.  The returned set is unmodifiable.
     */
    @Nonnull
    public abstract ImmutableSet<PlainPropertyValue> getPropertyValues();

    @Nonnull
    public abstract EntityFrame<? extends OWLEntityData> toEntityFrame(PrimitiveRenderer renderer);
}

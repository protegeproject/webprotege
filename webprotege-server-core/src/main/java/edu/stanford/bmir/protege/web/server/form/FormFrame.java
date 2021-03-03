package edu.stanford.bmir.protege.web.server.form;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.data.FormEntitySubject;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-13
 *
 * Represents the translation of form data into frame data
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class FormFrame {

    @Nonnull
    public static FormFrame get(@Nonnull FormEntitySubject formSubject,
                                @Nonnull ImmutableSet<OWLClass> parents,
                                @Nonnull ImmutableSet<OWLClass> subClasses,
                                @Nonnull ImmutableSet<OWLNamedIndividual> instances,
                                @Nonnull ImmutableSet<PlainPropertyValue> propertyValues,
                                @Nonnull ImmutableSet<FormFrame> nestedFrames) {
        return new AutoValue_FormFrame(formSubject, parents, subClasses, instances, propertyValues, nestedFrames);
    }

    @Nonnull
    public static FormFrame get(@Nonnull FormEntitySubject formSubject) {
        return get(formSubject,
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of(),
                   ImmutableSet.of());
    }

    @Nonnull
    public abstract FormEntitySubject getSubject();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getClasses();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getSubClasses();

    @Nonnull
    public abstract ImmutableSet<OWLNamedIndividual> getInstances();

    @Nonnull
    public abstract ImmutableSet<PlainPropertyValue> getPropertyValues();

    @Nonnull
    public abstract ImmutableSet<FormFrame> getNestedFrames();
}

package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class MovedClasses implements StructuredChangeDescription {

    public static MovedClasses get(@Nonnull ImmutableSet<OWLClass> classes,
                                   @Nonnull ImmutableSet<OWLClass> from,
                                   @Nonnull OWLClass to) {
        return new AutoValue_MovedClasses(classes, from, to);
    }

    @Nonnull
    public abstract ImmutableSet<OWLClass> getClasses();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getFrom();

    @Nonnull
    public abstract OWLClass getTo();

    @Nonnull
    @Override
    public String getTypeName() {
        return "MovedClasses";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        if(getClasses().size() == 1) {
            return formatter.formatString("Moved class %s from %s to %s", getClasses(), getFrom(), getTo());
        }
        else {
            return formatter.formatString("Moved classes %s from %s to %s", getClasses(), getFrom(), getTo());
        }
    }
}

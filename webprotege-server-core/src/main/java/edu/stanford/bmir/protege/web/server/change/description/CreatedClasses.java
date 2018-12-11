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
public abstract class CreatedClasses implements StructuredChangeDescription {

    private static final String CREATED_CLASSES = "CreatedClasses";

    public static String getAssociatedTypeName() {
        return CREATED_CLASSES;
    }

    @Nonnull
    public static CreatedClasses get(@Nonnull ImmutableSet<OWLClass> classes,
                                     @Nonnull ImmutableSet<OWLClass> parentClasses) {
        return new AutoValue_CreatedClasses(classes, parentClasses);
    }

    @Nonnull
    public abstract ImmutableSet<OWLClass> getClasses();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getParentClasses();


    @Nonnull
    @Override
    public String getTypeName() {
        return getAssociatedTypeName();
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        if(getParentClasses().isEmpty()) {
            if(getClasses().size() == 1) {
                return formatter.formatString("Created class %s", getClasses());
            }
            else {
                return formatter.formatString("Created classes %s", getClasses());
            }
        }
        else {
            if(getClasses().size() == 1) {
                return formatter.formatString("Created %s as a subclass of %s", getClasses(), getParentClasses());
            }
            else {
                return formatter.formatString("Created %s as subclasses of %s", getClasses(), getParentClasses());
            }
        }
    }
}

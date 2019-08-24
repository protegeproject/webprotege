package edu.stanford.bmir.protege.web.server.change.description;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
public abstract class AbstractMovedProperties implements StructuredChangeDescription {

    @Nonnull
    public abstract ImmutableSet<? extends OWLProperty> getProperties();

    @Nonnull
    public abstract ImmutableSet<? extends OWLProperty> getFrom();

    @Nonnull
    public abstract OWLProperty getTo();

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        if(getProperties().size() == 1) {
            return formatter.formatString("Moved property %s from %s to %s", getProperties(), getFrom(), getTo());
        }
        else {
            return formatter.formatString("Moved properties %s from %s to %s", getProperties(), getFrom(), getTo());
        }
    }
}

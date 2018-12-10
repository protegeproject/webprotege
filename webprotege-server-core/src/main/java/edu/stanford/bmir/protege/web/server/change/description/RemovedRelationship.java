package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class RemovedRelationship implements StructuredChangeDescription {

    public static RemovedRelationship get(@Nonnull OWLObject subject,
                                          @Nonnull OWLProperty property,
                                          @Nonnull OWLObject value) {
        return new AutoValue_RemovedRelationship(subject,
                                                 property,
                                                 value);
    }

    @Nonnull
    public abstract OWLObject getSubject();

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    public abstract OWLObject getValue();

    @Nonnull
    @Override
    public String getTypeName() {
        return "AddedRelationship";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Added relationship (%s %s) on %s", getProperty(), getValue(), getSubject());
    }
}

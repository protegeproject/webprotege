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
public abstract class AddedPropertyRange implements StructuredChangeDescription {

    public static AddedPropertyRange get(@Nonnull OWLProperty property,
                                           @Nonnull OWLObject range) {
        return new AutoValue_AddedPropertyRange(property, range);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "AddedPropertyRange";
    }

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    public abstract OWLObject getRange();
    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Added %s to the range of %s", getRange(), getProperty());
    }
}

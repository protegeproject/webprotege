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
public abstract class EditedRelationshipValue implements StructuredChangeDescription {

    @Nonnull
    public static EditedRelationshipValue get(@Nonnull OWLObject subject,
                                              @Nonnull OWLProperty property,
                                              @Nonnull OWLObject fromValue,
                                              @Nonnull OWLObject toValue) {
        return new AutoValue_EditedRelationshipValue(subject, property, fromValue, toValue);
    }

    @Nonnull
    public abstract OWLObject getSubject();

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    public abstract OWLObject getFromValue();

    @Nonnull
    public abstract OWLObject getToValue();

    @Nonnull
    @Override
    public String getTypeName() {
        return "EditedRelationshipValue";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Changed the value of the %s relationship from %s to %s on %s" ,
                                      getProperty(),
                                      getFromValue(),
                                      getToValue(),
                                      getSubject());
    }
}

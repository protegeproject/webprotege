package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class EditedAnnotationValue implements StructuredChangeDescription {

    public static EditedAnnotationValue get(@Nonnull IRI subject,
                                            @Nonnull OWLAnnotationProperty property,
                                            @Nonnull OWLAnnotationValue fromValue,
                                            @Nonnull OWLAnnotationValue toValue) {
        return new AutoValue_EditedAnnotationValue(subject,
                                                   property,
                                                   fromValue,
                                                   toValue);
    }

    public abstract IRI getSubject();

    public abstract OWLAnnotationProperty getProperty();

    public abstract OWLAnnotationValue getFromValue();

    public abstract OWLAnnotationValue getToValue();

    @Nonnull
    @Override
    public String getTypeName() {
        return "EditedAnnotationValue";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Changed the value of %s from %s to %s on %s",
                                      getProperty(),
                                      getFromValue(),
                                      getToValue(),
                                      getSubject());
    }
}

package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import edu.stanford.bmir.protege.web.shared.frame.ObjectPropertyCharacteristic;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class SetObjectPropertyCharacteristic implements StructuredChangeDescription {

    public SetObjectPropertyCharacteristic get(@Nonnull OWLObjectProperty property,
                                               @Nonnull ObjectPropertyCharacteristic characteristic) {
        return new AutoValue_SetObjectPropertyCharacteristic(property, characteristic);
    }

    @Nonnull
    public abstract OWLObjectProperty getProperty();

    @Nonnull
    public abstract ObjectPropertyCharacteristic getCharacteristic();

    @Nonnull
    @Override
    public String getTypeName() {
        return "SetObjectPropertyCharacteristic";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Made property %s %s", getProperty(), getCharacteristic().getDisplayName());
    }
}

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
public abstract class RemovedPropertyDomain implements StructuredChangeDescription {

    public static RemovedPropertyDomain get(@Nonnull OWLProperty property,
                                           @Nonnull OWLObject range) {
        return new AutoValue_RemovedPropertyDomain(property, range);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "RemovedPropertyDomain";
    }

    @Nonnull
    public abstract OWLProperty getProperty();

    @Nonnull
    public abstract OWLObject getDomain();

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Removed %s from the domain of %s", getDomain(), getProperty());
    }
}

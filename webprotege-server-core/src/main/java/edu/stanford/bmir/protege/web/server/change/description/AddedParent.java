package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class AddedParent implements StructuredChangeDescription {

    public static AddedParent get(@Nonnull OWLEntity child,
                                    @Nonnull OWLEntity parent) {
        return new AutoValue_AddedParent(child, parent);
    }

    @Nonnull
    @Override
    public String getTypeName() {
        return "AddedParent";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Added parent %s from %s", getParent(), getChild());
    }

    @Nonnull
    public abstract OWLEntity getChild();

    @Nonnull
    public abstract OWLEntity getParent();
}

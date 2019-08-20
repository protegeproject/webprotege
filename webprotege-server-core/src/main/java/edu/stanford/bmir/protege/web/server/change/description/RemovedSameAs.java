package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class RemovedSameAs implements StructuredChangeDescription {

    public static RemovedSameAs get(@Nonnull ImmutableSet<OWLIndividual> individuals) {
        return new AutoValue_RemovedSameAs(individuals);
    }

    public abstract ImmutableSet<OWLIndividual> getIndividuals();

    @Nonnull
    @Override
    public String getTypeName() {
        return "RemovedSameAs";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Removed SameAs between %s", getIndividuals());
    }
}

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
public abstract class AddedSameAs implements StructuredChangeDescription {

    public static AddedSameAs get(@Nonnull ImmutableSet<OWLIndividual> individuals) {
        return new AutoValue_AddedSameAs(individuals);
    }

    public abstract ImmutableSet<OWLIndividual> getIndividuals();

    @Nonnull
    @Override
    public String getTypeName() {
        return "AddedSameAs";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        return formatter.formatString("Added SameAs between %s", getIndividuals());
    }
    
}

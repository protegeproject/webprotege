package edu.stanford.bmir.protege.web.server.change.description;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-10
 */
@AutoValue
public abstract class CreatedIndividuals implements StructuredChangeDescription {

    public static CreatedIndividuals get(@Nonnull ImmutableSet<OWLNamedIndividual> individuals,
                                         @Nonnull ImmutableSet<OWLClass> types) {
        return new AutoValue_CreatedIndividuals(individuals, types);
    }

    @Nonnull
    public abstract ImmutableSet<OWLNamedIndividual> getIndividuals();

    @Nonnull
    public abstract ImmutableSet<OWLClass> getTypes();

    @Nonnull
    @Override
    public String getTypeName() {
        return "CreatedIndividuals";
    }

    @Nonnull
    @Override
    public String formatDescription(@Nonnull OWLObjectStringFormatter formatter) {
        if(getTypes().isEmpty()) {
            if(getIndividuals().size() == 1) {
                return formatter.formatString("Created individual %s", getIndividuals());
            }
            else {
                return formatter.formatString("Created individuals %s", getIndividuals());
            }
        }
        else {
            if(getIndividuals().size() == 1) {
                return formatter.formatString("Created %s as an instance of %s", getIndividuals(), getTypes());
            }
            else {
                return formatter.formatString("Created %s as instances of %s", getIndividuals(), getTypes());
            }
        }
    }
}

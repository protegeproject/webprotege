package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(IndividualNameFieldDescriptor.TYPE)
public class IndividualNameFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "IndividualName";

    private ImmutableSet<OWLClass> filteringTypes;

    public IndividualNameFieldDescriptor(@Nonnull Set<OWLClass> filteringTypes) {
        this.filteringTypes = ImmutableSet.copyOf(checkNotNull(filteringTypes));
    }

    @GwtSerializationConstructor
    private IndividualNameFieldDescriptor() {
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    public Set<OWLClass> getFilteringTypes() {
        return filteringTypes;
    }
}

package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.individuals.InstanceRetrievalMode;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
@JsonTypeName(IndividualNameFieldDescriptor.TYPE)
public class IndividualNameFieldDescriptor implements FormFieldDescriptor {

    protected static final String TYPE = "INDIVIDUAL_NAME";

    private ImmutableSet<OWLClass> filteringTypes;

    private InstanceRetrievalMode retrievalMode;

    private LanguageMap placeholder = LanguageMap.empty();

    public IndividualNameFieldDescriptor(@Nonnull Set<OWLClass> filteringTypes,
                                         @Nonnull InstanceRetrievalMode retrievalMode,
                                         @Nonnull LanguageMap placeholder) {
        this.filteringTypes = ImmutableSet.copyOf(checkNotNull(filteringTypes));
        this.retrievalMode = checkNotNull(retrievalMode);
        this.placeholder = checkNotNull(placeholder);
    }

    @GwtSerializationConstructor
    private IndividualNameFieldDescriptor() {
    }

    public static String getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public String getAssociatedType() {
        return TYPE;
    }

    public Set<OWLClass> getFilteringTypes() {
        return filteringTypes;
    }

    public LanguageMap getPlaceholder() {
        return placeholder;
    }

    public InstanceRetrievalMode getRetrievalMode() {
        return retrievalMode;
    }
}

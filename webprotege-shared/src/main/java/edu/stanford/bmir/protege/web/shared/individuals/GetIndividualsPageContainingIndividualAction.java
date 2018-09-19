package edu.stanford.bmir.protege.web.shared.individuals;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Sep 2018
 */
public class GetIndividualsPageContainingIndividualAction implements ProjectAction<GetIndividualsPageContainingIndividualResult> {

    private ProjectId projectId;

    private OWLNamedIndividual individual;

    @Nullable
    private OWLClass preferredType;

    private InstanceRetrievalMode preferredMode;

    public GetIndividualsPageContainingIndividualAction(@Nonnull ProjectId projectId,
                                                        @Nonnull OWLNamedIndividual individual,
                                                        @Nonnull Optional<OWLClass> preferredType,
                                                        @Nullable InstanceRetrievalMode preferredMode) {
        this.projectId = checkNotNull(projectId);
        this.individual = checkNotNull(individual);
        this.preferredType = checkNotNull(preferredType).orElse(null);
        this.preferredMode = checkNotNull(preferredMode);
    }

    @GwtSerializationConstructor
    private GetIndividualsPageContainingIndividualAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLNamedIndividual getIndividual() {
        return individual;
    }

    @Nonnull
    public Optional<OWLClass> getPreferredType() {
        return Optional.ofNullable(preferredType);
    }

    @Nonnull
    public InstanceRetrievalMode getPreferredMode() {
        return preferredMode;
    }
}

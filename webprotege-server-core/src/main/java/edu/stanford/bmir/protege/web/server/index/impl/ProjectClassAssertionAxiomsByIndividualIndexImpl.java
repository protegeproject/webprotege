package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@ProjectSingleton
public class ProjectClassAssertionAxiomsByIndividualIndexImpl implements ProjectClassAssertionAxiomsByIndividualIndex, DependentIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex;

    @Inject
    public ProjectClassAssertionAxiomsByIndividualIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                                                            @Nonnull ClassAssertionAxiomsByIndividualIndex classAssertionAxiomsByIndividualIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.classAssertionAxiomsByIndividualIndex = checkNotNull(classAssertionAxiomsByIndividualIndex);
    }

    @Nonnull
    @Override
    public Collection<Index> getDependencies() {
        return List.of(projectOntologiesIndex, classAssertionAxiomsByIndividualIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(@Nonnull OWLIndividual individual) {
        checkNotNull(individual);
        return projectOntologiesIndex.getOntologyIds()
                .flatMap(ontId -> classAssertionAxiomsByIndividualIndex.getClassAssertionAxioms(individual, ontId));
    }
}

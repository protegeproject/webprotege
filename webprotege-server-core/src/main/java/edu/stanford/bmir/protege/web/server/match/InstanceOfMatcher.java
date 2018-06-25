package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jun 2018
 */
@AutoFactory
public class InstanceOfMatcher implements Matcher<OWLEntity> {

    @Nonnull
    private final ClassHierarchyProvider hierarchyProvider;

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLClass target;

    @Nonnull
    private final HierarchyFilterType filterType;

    private boolean initialised = false;

    private Set<OWLNamedIndividual> instances;

    public InstanceOfMatcher(@Nonnull @Provided ClassHierarchyProvider hierarchyProvider,
                             @Nonnull @Provided OWLOntology rootOntology,
                             @Nonnull OWLClass target,
                             @Nonnull HierarchyFilterType filterType) {
        this.hierarchyProvider = checkNotNull(hierarchyProvider);
        this.rootOntology = rootOntology;
        this.target = checkNotNull(target);
        this.filterType = checkNotNull(filterType);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if (!initialised) {
            if (filterType == HierarchyFilterType.DIRECT) {
                instances = rootOntology.getImportsClosure().stream()
                                        .flatMap(ont -> ont.getClassAssertionAxioms(target).stream())
                                        .map(OWLClassAssertionAxiom::getIndividual)
                                        .filter(OWLIndividual::isNamed)
                                        .map(ind -> (OWLNamedIndividual) ind)
                                        .collect(toSet());
            }
            else {
                if(target.isOWLThing()) {
                    instances = rootOntology.getIndividualsInSignature(Imports.INCLUDED);
                }
                else {
                    Set<OWLClass> clses = hierarchyProvider.getDescendants(target);
                    clses.add(target);
                    instances = clses.stream()
                                     .flatMap(cls -> rootOntology.getImportsClosure().stream()
                                                                 .flatMap(ont -> ont.getClassAssertionAxioms(cls).stream()))
                                     .map(OWLClassAssertionAxiom::getIndividual)
                                     .filter(OWLIndividual::isNamed)
                                     .map(ind -> (OWLNamedIndividual) ind)
                                     .collect(toSet());
                }
            }
            initialised = true;
        }
        return instances.contains(value);
    }
}

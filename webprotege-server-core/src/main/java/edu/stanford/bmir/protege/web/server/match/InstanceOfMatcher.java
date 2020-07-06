package edu.stanford.bmir.protege.web.server.match;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProviderImpl;
import edu.stanford.bmir.protege.web.server.index.ClassAssertionAxiomsByClassIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureByTypeIndex;
import edu.stanford.bmir.protege.web.shared.match.criteria.HierarchyFilterType;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

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
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final ClassAssertionAxiomsByClassIndex classAssertionsByClass;

    @Nonnull
    private final ProjectSignatureByTypeIndex projectSignatureByType;

    @Nonnull
    private final OWLClass target;

    @Nonnull
    private final HierarchyFilterType filterType;

    private boolean initialised = false;

    private Set<OWLNamedIndividual> instances;

    public InstanceOfMatcher(@Nonnull @Provided ClassHierarchyProvider hierarchyProvider,
                             @Nonnull @Provided ProjectOntologiesIndex projectOntologiesIndex,
                             @Nonnull @Provided ClassAssertionAxiomsByClassIndex classAssertionsByClass,
                             @Nonnull @Provided ProjectSignatureByTypeIndex projectSignatureByType,
                             @Nonnull OWLClass target,
                             @Nonnull HierarchyFilterType filterType) {
        this.hierarchyProvider = checkNotNull(hierarchyProvider);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.classAssertionsByClass = checkNotNull(classAssertionsByClass);
        this.projectSignatureByType = checkNotNull(projectSignatureByType);
        this.target = checkNotNull(target);
        this.filterType = checkNotNull(filterType);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean matches(@Nonnull OWLEntity value) {
        if(!initialised) {
            if(filterType == HierarchyFilterType.DIRECT) {
                instances = getClassAssertionAxioms(target)
                                                  .map(OWLClassAssertionAxiom::getIndividual)
                                                  .filter(OWLIndividual::isNamed)
                                                  .map(ind -> (OWLNamedIndividual) ind)
                                                  .collect(toSet());
            }
            else {
                if(target.isOWLThing()) {
                    instances = projectSignatureByType.getSignature(EntityType.NAMED_INDIVIDUAL)
                                                      .collect(toSet());
                }
                else {
                    Collection<OWLClass> clses = hierarchyProvider.getDescendants(target);
                    clses.add(target);
                    instances = clses.stream()
                                     .flatMap(this::getClassAssertionAxioms)
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

    private Stream<OWLClassAssertionAxiom> getClassAssertionAxioms(OWLClass cls) {
        return projectOntologiesIndex.getOntologyIds()
                                     .flatMap(ontId -> classAssertionsByClass.getClassAssertionAxioms(
                                             cls,
                                             ontId));
    }
}

package edu.stanford.bmir.protege.web.server.usage;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class ReferencingAxiomVisitor implements OWLAxiomVisitorEx<Set<UsageReference>> {

    @Nonnull
    private final OWLEntity usageOf;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInSignatureIndex;

    @Inject
    @AutoFactory
    public ReferencingAxiomVisitor(@Nonnull OWLEntity usageOf,
                                   @Provided @Nonnull RenderingManager renderingManager,
                                   @Provided @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInSignatureIndex) {
        this.usageOf = checkNotNull(usageOf);
        this.renderingManager = checkNotNull(renderingManager);
        this.entitiesInSignatureIndex = checkNotNull(entitiesInSignatureIndex);
    }

    private Set<UsageReference> translate(Collection<? extends OWLObject> subjects, OWLAxiom axiom) {
        return subjects.stream()
                .flatMap(subject -> translate(subject, axiom).stream())
                .collect(toSet());
    }

    private Set<UsageReference> translate(OWLObject subject, OWLAxiom axiom) {
        Optional<OWLEntity> axiomSubject = Optional.empty();
        if(subject instanceof OWLEntity) {
            axiomSubject = Optional.of((OWLEntity) subject);
        }
        else if(subject instanceof IRI) {
            if(subject.equals(usageOf.getIRI())) {
                return translate(usageOf, axiom);
            }
            else {
                var entities = entitiesInSignatureIndex
                        .getEntitiesInSignature((IRI) subject)
                        .collect(toList());
                return translate(entities, axiom);
            }

        }
        else if(subject instanceof SWRLAtom) {
            var predicate = ((SWRLAtom) subject).getPredicate();
            if (predicate instanceof OWLEntity) {
                return translate((OWLEntity) predicate, axiom);
            }
        }
        var useageOfBrowserText = renderingManager.getShortForm(usageOf);
        var rendering = renderingManager.getHTMLBrowserText(axiom, Collections.singleton(useageOfBrowserText));
        var subjectRendering = axiomSubject.map(renderingManager::getShortForm);
        return Collections.singleton(new UsageReference(axiom.getAxiomType(), rendering, axiomSubject, subjectRendering));
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSubClassOfAxiom axiom) {
        return translate(axiom.getSubClass(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLAsymmetricObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLReflexiveObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDisjointClassesAxiom axiom) {
        return translate((OWLEntity) null, axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDataPropertyDomainAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLObjectPropertyDomainAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLEquivalentObjectPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLNegativeDataPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDifferentIndividualsAxiom axiom) {
        return translate((OWLEntity) null, axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDisjointDataPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDisjointObjectPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLObjectPropertyRangeAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLObjectPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLFunctionalObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSubObjectPropertyOfAxiom axiom) {
        return translate(axiom.getSubProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDisjointUnionAxiom axiom) {
        return translate(axiom.getOWLClass(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDeclarationAxiom axiom) {
        return translate(axiom.getEntity(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLAnnotationAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSymmetricObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDataPropertyRangeAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLFunctionalDataPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLEquivalentDataPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLClassAssertionAxiom axiom) {
        return translate(axiom.getIndividual(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLEquivalentClassesAxiom axiom) {
        if(axiom.containsNamedEquivalentClass()) {
            return translate(axiom.getNamedClasses(), axiom);
        }
        else {
            return translate(axiom.getClassExpressions().iterator().next(), axiom);
        }

    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDataPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLTransitiveObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLIrreflexiveObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSubDataPropertyOfAxiom axiom) {
        return translate(axiom.getSubProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSameIndividualAxiom axiom) {
        return translate(axiom.getIndividuals(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSubPropertyChainOfAxiom axiom) {
        return translate(axiom.getSuperProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLInverseObjectPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLHasKeyAxiom axiom) {
        return translate(axiom.getClassExpression(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLDatatypeDefinitionAxiom axiom) {
        return translate(axiom.getDatatype(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull SWRLRule rule) {
        return translate(rule.getHead(), rule);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLSubAnnotationPropertyOfAxiom axiom) {
        return translate(axiom.getSubProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLAnnotationPropertyDomainAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Nonnull
    @Override
    public Set<UsageReference> visit(@Nonnull OWLAnnotationPropertyRangeAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }
}

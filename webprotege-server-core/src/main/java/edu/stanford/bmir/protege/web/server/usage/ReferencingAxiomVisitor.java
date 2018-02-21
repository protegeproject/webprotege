package edu.stanford.bmir.protege.web.server.usage;

import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

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
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    public ReferencingAxiomVisitor(@Nonnull OWLEntity usageOf,
                                   @Nonnull OWLOntology rootOntology,
                                   @Nonnull RenderingManager renderingManager) {
        this.usageOf = checkNotNull(usageOf);
        this.rootOntology = checkNotNull(rootOntology);
        this.renderingManager = checkNotNull(renderingManager);
    }

    private Set<UsageReference> translate(Set<? extends OWLObject> subjects, OWLAxiom axiom) {
        Set<UsageReference> result = new HashSet<>();
        for(OWLObject subject : subjects) {
            result.addAll(translate(subject, axiom));
        }
        return result;
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
                final Set<OWLEntity> entities = rootOntology.getEntitiesInSignature((IRI) subject,
                                                                                                 Imports.INCLUDED);
                return translate(entities, axiom);
            }

        }
        else if(subject instanceof SWRLAtom) {
            SWRLPredicate predicate = ((SWRLAtom) subject).getPredicate();
            if (predicate instanceof OWLEntity) {
                return translate((OWLEntity) predicate, axiom);
            }
        }
        final String useageOfBrowserText = renderingManager.getBrowserText(usageOf);
        String rendering = renderingManager.getHTMLBrowserText(axiom, Collections.singleton(useageOfBrowserText));
        Optional<String> subjectRendering;
        if(axiomSubject.isPresent()) {
            subjectRendering = Optional.of(renderingManager.getBrowserText(axiomSubject.get()));
        }
        else {
            subjectRendering = Optional.empty();
        }
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

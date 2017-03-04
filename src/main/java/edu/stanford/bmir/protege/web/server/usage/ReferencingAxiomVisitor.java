package edu.stanford.bmir.protege.web.server.usage;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.project.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public class ReferencingAxiomVisitor implements OWLAxiomVisitorEx<Set<UsageReference>> {

    private OWLAPIProject project;

    private OWLEntity usageOf;

    public ReferencingAxiomVisitor(OWLAPIProject project, OWLEntity usageOf) {
        this.project = checkNotNull(project);
        this.usageOf = checkNotNull(usageOf);
    }

    private Set<UsageReference> translate(Set<? extends OWLObject> subjects, OWLAxiom axiom) {
        Set<UsageReference> result = new HashSet<UsageReference>();
        for(OWLObject subject : subjects) {
            result.addAll(translate(subject, axiom));
        }
        return result;
    }

    private Set<UsageReference> translate(OWLObject subject, OWLAxiom axiom) {
        Optional<OWLEntity> axiomSubject = Optional.absent();
        if(subject instanceof OWLEntity) {
            axiomSubject = Optional.of((OWLEntity) subject);
        }
        else if(subject instanceof IRI) {
            if(subject.equals(usageOf.getIRI())) {
                return translate(usageOf, axiom);
            }
            else {
                final Set<OWLEntity> entities = project.getRootOntology().getEntitiesInSignature((IRI) subject, true);
                return translate(entities, axiom);
            }

        }
        else if(subject instanceof SWRLAtom) {
            SWRLPredicate predicate = ((SWRLAtom) subject).getPredicate();
            if (predicate instanceof OWLEntity) {
                return translate((OWLEntity) predicate, axiom);
            }
        }
        final String useageOfBrowserText = project.getRenderingManager().getBrowserText(usageOf);
        String rendering = project.getRenderingManager().getHTMLBrowserText(axiom, Collections.singleton(useageOfBrowserText));
        Optional<String> subjectRendering;
        if(axiomSubject.isPresent()) {
            subjectRendering = Optional.of(project.getRenderingManager().getBrowserText(axiomSubject.get()));
        }
        else {
            subjectRendering = Optional.absent();
        }
        return Collections.singleton(new UsageReference(axiom.getAxiomType(), rendering, axiomSubject, subjectRendering));
    }

    @Override
    public Set<UsageReference> visit(OWLSubClassOfAxiom axiom) {
        return translate(axiom.getSubClass(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLReflexiveObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDisjointClassesAxiom axiom) {
        return translate((OWLEntity) null, axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDataPropertyDomainAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLObjectPropertyDomainAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDifferentIndividualsAxiom axiom) {
        return translate((OWLEntity) null, axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDisjointDataPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDisjointObjectPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLObjectPropertyRangeAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLObjectPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLFunctionalObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLSubObjectPropertyOfAxiom axiom) {
        return translate(axiom.getSubProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDisjointUnionAxiom axiom) {
        return translate(axiom.getOWLClass(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDeclarationAxiom axiom) {
        return translate(axiom.getEntity(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLAnnotationAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLSymmetricObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDataPropertyRangeAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLFunctionalDataPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLEquivalentDataPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLClassAssertionAxiom axiom) {
        return translate(axiom.getIndividual(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLEquivalentClassesAxiom axiom) {
        if(axiom.containsNamedEquivalentClass()) {
            return translate(axiom.getNamedClasses(), axiom);
        }
        else {
            return translate(axiom.getClassExpressions().iterator().next(), axiom);
        }

    }

    @Override
    public Set<UsageReference> visit(OWLDataPropertyAssertionAxiom axiom) {
        return translate(axiom.getSubject(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLTransitiveObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLSubDataPropertyOfAxiom axiom) {
        return translate(axiom.getSubProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLSameIndividualAxiom axiom) {
        return translate(axiom.getIndividuals(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLSubPropertyChainOfAxiom axiom) {
        return translate(axiom.getSuperProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLInverseObjectPropertiesAxiom axiom) {
        return translate(axiom.getProperties(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLHasKeyAxiom axiom) {
        return translate(axiom.getClassExpression(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLDatatypeDefinitionAxiom axiom) {
        return translate(axiom.getDatatype(), axiom);
    }

    @Override
    public Set<UsageReference> visit(SWRLRule rule) {
        return translate(rule.getHead(), rule);
    }

    @Override
    public Set<UsageReference> visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        return translate(axiom.getSubProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLAnnotationPropertyDomainAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }

    @Override
    public Set<UsageReference> visit(OWLAnnotationPropertyRangeAxiom axiom) {
        return translate(axiom.getProperty(), axiom);
    }
}

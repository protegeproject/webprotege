package edu.stanford.bmir.protege.web.server.obo;

import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.coode.owlapi.obo.parser.OBOPrefix;
import org.semanticweb.owlapi.model.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/07/2012
 */
public class OBOOntologyChecker {

    /**
     * Uses some heuristics to determine if the specified ontology and its imports closure is an OBO ontology.
     * @param ontology The ontology to check
     * @return <code>true</code> if the ontology is an OBO ontology (or could be an OBO ontology) otherwise false.
     */
    public boolean isOBOOntology(OWLOntology ontology) {
        if(isOBOFormat(ontology)) {
            return true;
        }
        for(AxiomType<?> axiomType : AxiomType.AXIOM_TYPES) {
            for(OWLAxiom ax : ontology.getAxioms(axiomType, true)) {
                if(!isOBOAxiom(ax)) {
                    return false;
                }
            }
        }
        for (OWLAnnotationProperty property : ontology.getAnnotationPropertiesInSignature()) {
            if (isOBOEntity(property)) {
                return true;
            }
        }
        return false;
    }


    private boolean isOBOFormat(OWLOntology ontology) {
        OWLOntologyManager man = ontology.getOWLOntologyManager();
        return man.getOntologyFormat(ontology) instanceof OBOOntologyFormat;
    }

    private OBOAxiomChecker checker = new OBOAxiomChecker();

    private boolean isOBOAxiom(OWLAxiom axiom) {
        return axiom.accept(checker);
    }

    private boolean isOBOEntity(OWLEntity entity) {
        String propertyIRI = entity.getIRI().toString();
        if (propertyIRI.startsWith(OBOPrefix.OBO.getPrefix())) {
            return true;
        }
        else if (propertyIRI.startsWith(OBOPrefix.OBO_IN_OWL.getPrefix())) {
            return true;
        }
        else if (propertyIRI.startsWith(OBOPrefix.IAO.getPrefix())) {
            return true;
        }
        return false;
    }



    private class OBOAxiomChecker implements OWLAxiomVisitorEx<Boolean> {
        
        private OBOClassExpressionChecker classExpressionChecker = new OBOClassExpressionChecker();

        private Boolean isOBOClassExpression(OWLClassExpression ce) {
            return ce.accept(classExpressionChecker);
        }
        
        public Boolean visit(OWLSubClassOfAxiom axiom) {
            return !axiom.getSubClass().isAnonymous() && isOBOClassExpression(axiom.getSuperClass());
        }

        public Boolean visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLAsymmetricObjectPropertyAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLReflexiveObjectPropertyAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLDisjointClassesAxiom axiom) {
            for(OWLClassExpression ce : axiom.getClassExpressions()) {
                if(ce.isAnonymous()) {
                    return false;
                }
            }
            return true;
        }

        public Boolean visit(OWLDataPropertyDomainAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLObjectPropertyDomainAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLEquivalentObjectPropertiesAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLDifferentIndividualsAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLDisjointDataPropertiesAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLDisjointObjectPropertiesAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLObjectPropertyRangeAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLObjectPropertyAssertionAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLFunctionalObjectPropertyAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLSubObjectPropertyOfAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLDisjointUnionAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLDeclarationAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLAnnotationAssertionAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLSymmetricObjectPropertyAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLDataPropertyRangeAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLFunctionalDataPropertyAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLEquivalentDataPropertiesAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLClassAssertionAxiom axiom) {
            return !axiom.getClassExpression().isAnonymous();
        }

        public Boolean visit(OWLEquivalentClassesAxiom axiom) {
            if(!axiom.containsNamedEquivalentClass()) {
                return false;
            }
            for(OWLClassExpression ce : axiom.getClassExpressions()) {
                if(!isOBOClassExpression(ce)) {
                    return false;
                }
            }
            return true;
        }

        public Boolean visit(OWLDataPropertyAssertionAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLTransitiveObjectPropertyAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLSubDataPropertyOfAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLSameIndividualAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLSubPropertyChainOfAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLInverseObjectPropertiesAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLHasKeyAxiom axiom) {
            return false;
        }

        public Boolean visit(OWLDatatypeDefinitionAxiom axiom) {
            return false;
        }

        public Boolean visit(SWRLRule rule) {
            return false;
        }

        public Boolean visit(OWLSubAnnotationPropertyOfAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLAnnotationPropertyDomainAxiom axiom) {
            return true;
        }

        public Boolean visit(OWLAnnotationPropertyRangeAxiom axiom) {
            return true;
        }
    }


    private static class OBOClassExpressionChecker implements OWLClassExpressionVisitorEx<Boolean> {

        public Boolean visit(OWLClass ce) {
            return true;
        }

        public Boolean visit(OWLObjectIntersectionOf ce) {
            for(OWLClassExpression op : ce.getOperands()) {
                if(!op.accept(this)) {
                    return false;
                }
            }
            return true;
        }

        public Boolean visit(OWLObjectUnionOf ce) {
            return false;
        }

        public Boolean visit(OWLObjectComplementOf ce) {
            return false;
        }

        public Boolean visit(OWLObjectSomeValuesFrom ce) {
            return !ce.isAnonymous();
        }

        public Boolean visit(OWLObjectAllValuesFrom ce) {
            return false;
        }

        public Boolean visit(OWLObjectHasValue ce) {
            // Hmmmm
            return true;
        }

        public Boolean visit(OWLObjectMinCardinality ce) {
            return false;
        }

        public Boolean visit(OWLObjectExactCardinality ce) {
            return false;
        }

        public Boolean visit(OWLObjectMaxCardinality ce) {
            return false;
        }

        public Boolean visit(OWLObjectHasSelf ce) {
            return false;
        }

        public Boolean visit(OWLObjectOneOf ce) {
            return ce.getIndividuals().size() == 1;
        }

        public Boolean visit(OWLDataSomeValuesFrom ce) {
            return false;
        }

        public Boolean visit(OWLDataAllValuesFrom ce) {
            return false;
        }

        public Boolean visit(OWLDataHasValue ce) {
            return false;
        }

        public Boolean visit(OWLDataMinCardinality ce) {
            return false;
        }

        public Boolean visit(OWLDataExactCardinality ce) {
            return false;
        }

        public Boolean visit(OWLDataMaxCardinality ce) {
            return false;
        }
    }


}

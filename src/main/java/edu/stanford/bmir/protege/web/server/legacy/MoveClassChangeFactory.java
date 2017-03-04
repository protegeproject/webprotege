package edu.stanford.bmir.protege.web.server.legacy;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 02/04/2012
 */
@Deprecated
public class MoveClassChangeFactory extends OWLOntologyChangeFactory {

    private String clsName;
    
    private String oldParent;
    
    private String newParent;

    public MoveClassChangeFactory(Project project, UserId userId, String changeDescription, String clsName, String oldParent, String newParent, boolean checkForCycles) {
        super(project, userId, changeDescription);
        this.clsName = clsName;
        this.oldParent = oldParent;
        this.newParent = newParent;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        LegacyEntityDataManager rm = getRenderingManager();
        OWLClass cls = rm.getEntity(clsName, EntityType.CLASS);
        OWLClass oldParentCls = rm.getEntity(oldParent, EntityType.CLASS);
        OWLClass newParentCls = rm.getEntity(newParent, EntityType.CLASS);

        // According to the hierarchy provider  A is a subclass of B if
        // SubClassOf(A B)
        // SubClassOf(A ObjectIntersectionOf(B ...))    (could be nested further)
        // EquivalentClasses(A B)
        // EquivalentClasses(A ObjectIntersectionOf(B, ...))    (could be nested further).
        
        // We therefore have to delve inside these axioms in order to "remove" a parent.  Once questions is, should the
        // new parent replace the old one directly or go in as a fresh subclassof axiom?

        for (OWLOntology ontology : getRootOntology().getImportsClosure()) {
            for(OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(cls)) {
                
                changeListToFill.add(new RemoveAxiom(ontology, ax));
                
                OWLClassExpression superCls = ax.getSuperClass();
                ClassExpressionMutator mutator = new ClassExpressionMutator(oldParentCls);
                OWLClassExpression newSuperCls = superCls.accept(mutator);
                if(newSuperCls != null) {
                    OWLSubClassOfAxiom sca = getDataFactory().getOWLSubClassOfAxiom(cls, newSuperCls, ax.getAnnotations());
                    changeListToFill.add(new AddAxiom(ontology, sca));       
                }
            }
            
            for(OWLEquivalentClassesAxiom ax : ontology.getEquivalentClassesAxioms(cls)) {
                Set<OWLClassExpression> newEquivalentClasses = new HashSet<OWLClassExpression>();
                for(OWLClassExpression equiv : ax.getClassExpressionsMinus(cls)) {
                    ClassExpressionMutator mutator = new ClassExpressionMutator(cls);
                    OWLClassExpression newEquivalentClass = equiv.accept(mutator);
                    if(newEquivalentClass != null) {
                        newEquivalentClasses.add(newEquivalentClass);
                    }
                }
                if(!newEquivalentClasses.isEmpty()) {
                    newEquivalentClasses.add(cls);
                    OWLEquivalentClassesAxiom eca = getDataFactory().getOWLEquivalentClassesAxiom(newEquivalentClasses, ax.getAnnotations());
                    changeListToFill.add(new AddAxiom(ontology, eca));
                }
            }
        }
        OWLSubClassOfAxiom newSca = getDataFactory().getOWLSubClassOfAxiom(cls, newParentCls);
        changeListToFill.add(new AddAxiom(getRootOntology(), newSca));

    }


    private class ClassExpressionMutator implements OWLClassExpressionVisitorEx<OWLClassExpression> {

        private OWLClass conjunctToRemove;

        private ClassExpressionMutator(OWLClass conjunctToRemove) {
            this.conjunctToRemove = conjunctToRemove;
        }

        public OWLClassExpression visit(OWLClass owlClass) {
            return owlClass.equals(conjunctToRemove) ? null : owlClass;
        }

        public OWLClassExpression visit(OWLObjectIntersectionOf owlObjectIntersectionOf) {
            Set<OWLClassExpression> replacementConjuncts = new HashSet<OWLClassExpression>();
            for(OWLClassExpression conjunct : owlObjectIntersectionOf.getOperands()) {
                OWLClassExpression newConjunct = conjunct.accept(this);
                if(newConjunct != null) {
                    replacementConjuncts.add(newConjunct);
                }
            }
            if(replacementConjuncts.size() == 1) {
                return replacementConjuncts.iterator().next();
            }
            else if(replacementConjuncts.size() > 1) {
                return getDataFactory().getOWLObjectIntersectionOf(replacementConjuncts);    
            }
            else {
                return null;
            }
        }

        public OWLClassExpression visit(OWLObjectUnionOf owlObjectUnionOf) {
            return owlObjectUnionOf;
        }

        public OWLClassExpression visit(OWLObjectComplementOf owlObjectComplementOf) {
            return owlObjectComplementOf;
        }

        public OWLClassExpression visit(OWLObjectSomeValuesFrom owlObjectSomeValuesFrom) {
            return owlObjectSomeValuesFrom;
        }

        public OWLClassExpression visit(OWLObjectAllValuesFrom owlObjectAllValuesFrom) {
            return owlObjectAllValuesFrom;
        }

        public OWLClassExpression visit(OWLObjectHasValue owlObjectHasValue) {
            return owlObjectHasValue;
        }

        public OWLClassExpression visit(OWLObjectMinCardinality owlObjectMinCardinality) {
            return owlObjectMinCardinality;
        }

        public OWLClassExpression visit(OWLObjectExactCardinality owlObjectExactCardinality) {
            return owlObjectExactCardinality;
        }

        public OWLClassExpression visit(OWLObjectMaxCardinality owlObjectMaxCardinality) {
            return owlObjectMaxCardinality;
        }

        public OWLClassExpression visit(OWLObjectHasSelf owlObjectHasSelf) {
            return owlObjectHasSelf;
        }

        public OWLClassExpression visit(OWLObjectOneOf owlObjectOneOf) {
            return owlObjectOneOf;
        }

        public OWLClassExpression visit(OWLDataSomeValuesFrom owlDataSomeValuesFrom) {
            return owlDataSomeValuesFrom;
        }

        public OWLClassExpression visit(OWLDataAllValuesFrom owlDataAllValuesFrom) {
            return owlDataAllValuesFrom;
        }

        public OWLClassExpression visit(OWLDataHasValue owlDataHasValue) {
            return owlDataHasValue;
        }

        public OWLClassExpression visit(OWLDataMinCardinality owlDataMinCardinality) {
            return owlDataMinCardinality;
        }

        public OWLClassExpression visit(OWLDataExactCardinality owlDataExactCardinality) {
            return owlDataExactCardinality;
        }

        public OWLClassExpression visit(OWLDataMaxCardinality owlDataMaxCardinality) {
            return owlDataMaxCardinality;
        }
    }
}

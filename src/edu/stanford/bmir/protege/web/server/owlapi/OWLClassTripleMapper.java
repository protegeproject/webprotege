package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/04/2012
 */
public class OWLClassTripleMapper extends TripleMapper<OWLClass> {

    public OWLClassTripleMapper(OWLAPIProject project, OWLClass entity, AnnotationsTreatment annotationsTreatment, NonAnnotationTreatment nonAnnotationTreatment) {
        super(project, entity, annotationsTreatment, nonAnnotationTreatment);

    }

    @Override
    public List<Triple> getNonAnnotationAssertionTriples() {

        final List<Triple> result = new ArrayList<Triple>();

        
        final RenderingManager rm = getRenderingManager();

        

        final OWLClass cls = getEntity();


        List<OWLClassExpression> ces = new ArrayList<OWLClassExpression>();
        ces.addAll(cls.getSuperClasses(getRootOntologyImportsClosure()));
        ces.addAll(cls.getEquivalentClasses(getRootOntologyImportsClosure()));

        for (OWLClassExpression classExpression : ces) {
            for (OWLClassExpression ce : classExpression.asConjunctSet()) {
                ce.accept(new OWLClassExpressionVisitorAdapter() {

                    @Override
                    public void visit(OWLObjectIntersectionOf desc) {
                        for(OWLClassExpression ce : desc.getOperands()) {
                            ce.accept(this);
                        }
                    }

                    @Override
                    public void visit(OWLObjectSomeValuesFrom ce) {
                        processAsExistential(ce);
                    }

                    @Override
                    public void visit(OWLObjectHasValue desc) {
                        desc.asSomeValuesFrom().accept(this);
                    }

                    @Override
                    public void visit(OWLObjectMinCardinality desc) {
                        processAsExistential(desc);
                    }

                    @Override
                    public void visit(OWLObjectExactCardinality desc) {
                        desc.asIntersectionOfMinMax().accept(this);
                    }

                    private OWLObjectProperty getSimpleNamedProperty(OWLRestriction<?, OWLObjectPropertyExpression, ?> ce) {
                        return ce.getProperty().getSimplified().asOWLObjectProperty();
                    }

                    private boolean isSimpleNamedPropertyRestriction(OWLRestriction<?, OWLObjectPropertyExpression, ?> ce) {
                        return !ce.getProperty().getSimplified().isAnonymous();
                    }

                    private void processAsExistential(OWLQuantifiedRestriction<OWLClassExpression, OWLObjectPropertyExpression, OWLClassExpression> ce) {
                        if (isSimpleNamedPropertyRestriction(ce)) {
                            for(OWLClassExpression filler : ce.getFiller().asConjunctSet()) {
                                OWLEntity entityFiller = filler.accept(new OWLClassExpressionVisitorExAdapter<OWLEntity>() {
                                    @Override
                                    public OWLEntity visit(OWLClass ce) {
                                        return ce;
                                    }

                                    @Override
                                    public OWLEntity visit(OWLObjectOneOf ce) {
                                        Set<OWLIndividual> individuals = ce.getIndividuals();
                                        if(individuals.size() == 1) {
                                            OWLIndividual ind = individuals.iterator().next();
                                            if(!ind.isAnonymous()) {
                                                return ind.asOWLNamedIndividual();
                                            }
                                        }
                                        return null;
                                    }
                                });

                                if(entityFiller != null) {
                                    OWLObjectProperty property = getSimpleNamedProperty(ce);
                                    PropertyEntityData propertyEntityData = rm.getPropertyEntityData(property);
                                    propertyEntityData.setMinCardinality(1);
                                    result.add(new Triple(rm.getEntityData(cls), propertyEntityData, rm.getEntityData(entityFiller)));
                                }

                            }
                        }
                    }


                });

            }
        }




        return result;

    }
}

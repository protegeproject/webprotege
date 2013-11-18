package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2012
 */
public class GetRelatedPropertiesStrategy extends OntologyServiceStrategy<List<Triple>> {
    
    private String className;

    public GetRelatedPropertiesStrategy(OWLAPIProject project, UserId userId, String className) {
        super(project, userId);
        this.className = className;
    }

    @Override
    public List<Triple> execute() {
        final List<Triple> result = new ArrayList<Triple>();
        
        final RenderingManager rm = getProject().getRenderingManager();
        for(OWLEntity entity : rm.getEntities(className)) {
            TripleMapperSelector selector = new TripleMapperSelector(getProject(), AnnotationsTreatment.INCLUDE_ANNOTATIONS, NonAnnotationTreatment.INCLUDE_NON_ANNOTATIONS);
            TripleMapper<?> mapper = selector.getMapper(entity);
            if (mapper != null) {
                result.addAll(mapper.getTriples());
            }
            // Show domains and ranges as well?  (I find this a bit yucky and I don't think we should do this)

            OWLOntology rootOntology = getRootOntology();
            Set<OWLAxiom> references = rootOntology.getReferencingAxioms(entity);
            for(OWLAxiom ref : references) {
                ref.accept(new OWLAxiomVisitorAdapter() {
                    @Override
                    public void visit(OWLDataPropertyDomainAxiom axiom) {
                    }

                    @Override
                    public void visit(OWLDataPropertyRangeAxiom axiom) {
                    }

                    @Override
                    public void visit(OWLObjectPropertyDomainAxiom axiom) {
                        final OWLClassExpression domain = axiom.getDomain();
                        if(domain.isAnonymous()) {
                            return;
                        }
                        final OWLObjectPropertyExpression property = axiom.getProperty();
                        if(property.isAnonymous()) {
                            return;
                        }
                        result.add(new Triple(rm.getEntityData(domain.asOWLClass()), rm.getPropertyEntityData(property.asOWLObjectProperty()), null));
                    }

                    @Override
                    public void visit(OWLObjectPropertyRangeAxiom axiom) {
                        final OWLClassExpression range = axiom.getRange();
                        if(range.isAnonymous()) {
                            return;
                        }
                        final OWLObjectPropertyExpression property = axiom.getProperty();
                        if(property.isAnonymous()) {
                            return;
                        }
                        result.add(new Triple(null, rm.getPropertyEntityData(property.asOWLObjectProperty()), rm.getEntityData(range.asOWLClass())));
                    }
                });
            }

        }


        return result;
    }
}

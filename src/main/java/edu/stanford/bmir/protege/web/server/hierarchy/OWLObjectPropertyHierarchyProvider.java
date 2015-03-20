package edu.stanford.bmir.protege.web.server.hierarchy;


import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 23-Jan-2007<br><br>
 */
public class OWLObjectPropertyHierarchyProvider extends AbstractOWLPropertyHierarchyProvider<OWLClassExpression, OWLObjectPropertyExpression, OWLObjectProperty> {

    @Inject
    public OWLObjectPropertyHierarchyProvider(@RootOntology OWLOntology rootOntology, @ObjectPropertyHierarchyRoot OWLObjectProperty root) {
        super(rootOntology, root);
    }


    protected Set<OWLObjectProperty> getPropertiesReferencedInChange(List<? extends OWLOntologyChange> changes) {
        Set<OWLObjectProperty> properties = new HashSet<OWLObjectProperty>();
        for (OWLOntologyChange change : changes) {
            if (change.isAxiomChange()) {
                OWLAxiomChange axiomChange = (OWLAxiomChange) change;
                for (OWLEntity entity : axiomChange.getEntities()) {
                    if (entity.isOWLObjectProperty()) {
                        properties.add(entity.asOWLObjectProperty());
                    }
                }
            }
        }
        return properties;
    }


    /**
     * Gets the relevant properties in the specified ontology that are contained
     * within the property hierarchy.  For example, for an object property hierarchy
     * this would constitute the set of referenced object properties in the specified
     * ontology.
     * @param ont The ontology
     */
    protected Set<? extends OWLObjectProperty> getReferencedProperties(OWLOntology ont) {
        return ont.getObjectPropertiesInSignature();
    }


    protected Set<? extends OWLSubPropertyAxiom> getSubPropertyAxiomForRHS(OWLObjectProperty prop, OWLOntology ont) {
        return ont.getObjectSubPropertyAxiomsForSuperProperty(prop);
    }


    protected boolean containsReference(OWLOntology ont, OWLObjectProperty prop) {
        return ont.containsObjectPropertyInSignature(prop.getIRI());
    }
}
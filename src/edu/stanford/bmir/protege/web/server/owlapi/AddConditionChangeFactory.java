package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItem;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class AddConditionChangeFactory extends OWLOntologyChangeFactory {

    private String className;
    
    private String conditionClassExpression;

    private AxiomType type;
    
    private List<ConditionItem> generatedItems = new ArrayList<ConditionItem>();
    
    public AddConditionChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String clsName, String conditionClassExpression, boolean isNS) {
        super(project, userId, changeDescription);
        this.className = clsName;
        this.conditionClassExpression = conditionClassExpression;
        if(isNS) {
            type = AxiomType.EQUIVALENT_CLASSES;
        }
        else {
            type = AxiomType.SUBCLASS_OF;
        }
    }


    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        try {
            OWLAPIProject project = getProject();
            ConditionItemParser parser = new ConditionItemParser(project);
            OWLClassExpression ce = parser.parse(conditionClassExpression);
            RenderingManager rm = project.getRenderingManager();
            OWLClass cls = rm.getEntity(className, EntityType.CLASS);
            OWLDataFactory df = project.getDataFactory();
            if(type == AxiomType.EQUIVALENT_CLASSES) {
                // Equivalent class
                OWLEquivalentClassesAxiom axiom = df.getOWLEquivalentClassesAxiom(cls, ce);
                changeListToFill.add(new AddAxiom(getRootOntology(), axiom));
                ConditionItemRenderer renderer = new ConditionItemRenderer(project, cls);
                generatedItems.add(renderer.getConditionItem(axiom));
            }
            else {
                OWLSubClassOfAxiom axiom = df.getOWLSubClassOfAxiom(cls, ce);
                changeListToFill.add(new AddAxiom(getRootOntology(), axiom));
                ConditionItemRenderer renderer = new ConditionItemRenderer(project, cls);
                generatedItems.add(renderer.getConditionItem(axiom));
            }
        }
        catch (ParserException e) {
            // Hmph
            throw new RuntimeException(e);
        }
    }
}

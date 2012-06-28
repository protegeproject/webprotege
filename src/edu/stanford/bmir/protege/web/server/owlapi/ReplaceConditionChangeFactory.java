package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItem;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class ReplaceConditionChangeFactory extends OWLOntologyChangeFactory {

    private String className;
    
    private ConditionItem conditionItem;
    
    private String newCondition;

    public ReplaceConditionChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String className, ConditionItem conditionItem, String newCondition) {
        super(project, userId, changeDescription);
        this.className = className;
        this.conditionItem = conditionItem;
        this.newCondition = newCondition;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        try {
            OWLClass rootClass = getRenderingManager().getEntity(className, EntityType.CLASS);
            ConditionItemRenderer renderer = new ConditionItemRenderer(getProject(), rootClass);
            OWLAxiom axiomToRemove = renderer.parseConditionItem(conditionItem);
            ConditionItemParser parser = new ConditionItemParser(getProject());
            OWLClassExpression ce = parser.parse(newCondition);
            OWLAxiom axiomToAdd;
            if(axiomToRemove instanceof OWLSubClassOfAxiom) {
                axiomToAdd = getDataFactory().getOWLSubClassOfAxiom(rootClass, ce, axiomToRemove.getAnnotations());
            }
            else {
                axiomToAdd = getDataFactory().getOWLEquivalentClassesAxiom(rootClass, ce, axiomToRemove.getAnnotations());
            }
            for(OWLOntology ont : getRootOntology().getImportsClosure()) {
                if(ont.containsAxiom(axiomToRemove)) {
                    changeListToFill.add(new RemoveAxiom(ont, axiomToRemove));
                    changeListToFill.add(new AddAxiom(ont, axiomToAdd));
                }
            }
            
        }
        catch (ParserException e) {
            throw new RuntimeException(e);
        }

    }
}

package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.ConditionItem;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.*;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2012
 */
public class DeleteConditionChangeFactory extends OWLOntologyChangeFactory {

    private String className;
    
    private ConditionItem conditionItem;

    public DeleteConditionChangeFactory(OWLAPIProject project, UserId userId, String changeDescription, String className, ConditionItem conditionItem) {
        super(project, userId, changeDescription);
        this.className = className;
        this.conditionItem = conditionItem;
    }

    @Override
    public void createChanges(List<OWLOntologyChange> changeListToFill) {
        try {
            OWLClass rootClass = getRenderingManager().getEntity(className, EntityType.CLASS);
            ConditionItemRenderer renderer = new ConditionItemRenderer(getProject(), rootClass);
            OWLAxiom axiom = renderer.parseConditionItem(conditionItem);
            for(OWLOntology ont : getRootOntology().getImportsClosure()) {
                if(ont.containsAxiom(axiom)) {
                    changeListToFill.add(new RemoveAxiom(ont, axiom));
                }
            }
        }
        catch (ParserException e) {
            throw new RuntimeException(e);
        }

    }
}

package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResultSection;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.action.GetEquivalentClassesAction;
import edu.stanford.protege.reasoning.action.GetEquivalentClassesResponse;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.reasoner.Node;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class EquivalentClassesSectionHandler extends DLQueryResultsSectionHandler<GetEquivalentClassesAction, GetEquivalentClassesResponse, OWLClass, Node<OWLClass>> {

    public EquivalentClassesSectionHandler() {
        super(DLQueryResultSection.EQUIVALENT_CLASSES);
    }

    @Override
    protected GetEquivalentClassesAction createAction(
            KbId kbId, OWLClassExpression ce) {
        return new GetEquivalentClassesAction(kbId, ce);
    }

    @Override
    protected ImmutableList<OWLClass> transform(Node<OWLClass> result) {
        return ImmutableList.copyOf(result.getEntities());
    }
}

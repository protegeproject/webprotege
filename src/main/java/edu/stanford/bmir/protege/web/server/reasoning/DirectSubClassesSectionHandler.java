package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResultSection;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.action.GetSubClassesAction;
import edu.stanford.protege.reasoning.action.GetSubClassesResponse;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class DirectSubClassesSectionHandler extends DLQueryResultsSectionHandler<GetSubClassesAction, GetSubClassesResponse, OWLClass, NodeSet<OWLClass>> {

    public DirectSubClassesSectionHandler() {
        super(DLQueryResultSection.DIRECT_SUBCLASSES);
    }

    @Override
    protected GetSubClassesAction createAction(
            KbId kbId, OWLClassExpression ce) {
        return new GetSubClassesAction(kbId, ce);
    }

    @Override
    protected ImmutableList<OWLClass> transform(NodeSet<OWLClass> result) {
        return ImmutableList.copyOf(result.getFlattened());
    }
}

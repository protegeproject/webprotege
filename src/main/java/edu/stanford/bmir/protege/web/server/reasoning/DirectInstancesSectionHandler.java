package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResultSection;
import edu.stanford.protege.reasoning.KbId;
import edu.stanford.protege.reasoning.action.GetInstancesAction;
import edu.stanford.protege.reasoning.action.GetInstancesResponse;
import edu.stanford.protege.reasoning.action.HierarchyQueryType;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class DirectInstancesSectionHandler extends DLQueryResultsSectionHandler<GetInstancesAction, GetInstancesResponse, OWLNamedIndividual, NodeSet<OWLNamedIndividual>> {

    public DirectInstancesSectionHandler() {
        super(DLQueryResultSection.DIRECT_INSTANCES);
    }

    @Override
    protected GetInstancesAction createAction(
            KbId kbId, OWLClassExpression ce) {
        return new GetInstancesAction(kbId, ce, HierarchyQueryType.DIRECT);
    }

    @Override
    protected ImmutableList<OWLNamedIndividual> transform(NodeSet<OWLNamedIndividual> result) {
        return ImmutableList.copyOf(result.getFlattened());
    }
}

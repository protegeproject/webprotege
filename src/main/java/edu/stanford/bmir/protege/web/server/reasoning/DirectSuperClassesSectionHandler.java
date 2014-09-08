package edu.stanford.bmir.protege.web.server.reasoning;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.reasoning.DLQueryResultSection;
import edu.stanford.protege.reasoning.KbId;/**/
import edu.stanford.protege.reasoning.action.*;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/09/2014
 */
public class DirectSuperClassesSectionHandler extends DLQueryResultsSectionHandler<GetSuperClassesAction,
        GetSuperClassesResponse, OWLClass, NodeSet<OWLClass>> {

    public DirectSuperClassesSectionHandler() {
        super(DLQueryResultSection.DIRECT_SUPERCLASSES);
    }

    @Override
    protected GetSuperClassesAction createAction(KbId kbId, OWLClassExpression ce) {
        return new GetSuperClassesAction(kbId, ce, HierarchyQueryType.DIRECT);
    }


    @Override
    protected ImmutableList<OWLClass> transform(NodeSet<OWLClass> result) {
        return ImmutableList.copyOf(result.getFlattened());
    }
}

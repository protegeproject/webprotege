package edu.stanford.bmir.protege.web.server.individuals;

import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.pagination.Pager;
import edu.stanford.bmir.protege.web.shared.entity.OWLNamedIndividualData;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsAction;
import edu.stanford.bmir.protege.web.shared.individualslist.GetIndividualsResult;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.inject.Inject;
import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsActionHandler extends AbstractHasProjectActionHandler<GetIndividualsAction, GetIndividualsResult> {

    @Inject
    public GetIndividualsActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected RequestValidator<GetIndividualsAction> getAdditionalRequestValidator(GetIndividualsAction action, RequestContext requestContext) {
        return NullValidator.get();
    }

    @Override
    protected GetIndividualsResult execute(GetIndividualsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        List<OWLNamedIndividualData> individualsData = new ArrayList<OWLNamedIndividualData>();
        if(action.getType().isOWLThing()) {
            Set<OWLNamedIndividual> individualsInSignature = project.getRootOntology().getIndividualsInSignature(true);
            for(OWLNamedIndividual individual : individualsInSignature) {
                individualsData.add(project.getRenderingManager().getRendering(individual));
            }
        }
        else {
            for(OWLClassAssertionAxiom ax : project.getRootOntology().getClassAssertionAxioms(action.getType())) {
                OWLIndividual individual = ax.getIndividual();
                if (individual.isNamed()) {
                    individualsData.add(project.getRenderingManager().getRendering(individual.asOWLNamedIndividual()));
                }
            }
        }
        Collections.sort(individualsData, new Comparator<OWLNamedIndividualData>() {
            @Override
            public int compare(OWLNamedIndividualData owlNamedIndividualData, OWLNamedIndividualData owlNamedIndividualData2) {
                return owlNamedIndividualData.getBrowserText().compareTo(owlNamedIndividualData2.getBrowserText());
            }
        });
        PageRequest pageRequest = action.getPageRequest();
        Pager<OWLNamedIndividualData> pager = Pager.getPagerForPageSize(individualsData, pageRequest.getPageSize());
        Page<OWLNamedIndividualData> page = pager.getPage(pageRequest.getPageNumber());
        return new GetIndividualsResult(page);
    }

    @Override
    public Class<GetIndividualsAction> getActionClass() {
        return GetIndividualsAction.class;
    }
}

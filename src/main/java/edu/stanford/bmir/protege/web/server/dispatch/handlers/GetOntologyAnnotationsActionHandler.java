package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.comparator.OntologyAnnotationsComparator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.ValidatorFactory;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Inject
    public GetOntologyAnnotationsActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetOntologyAnnotationsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetOntologyAnnotationsResult execute(GetOntologyAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        List<OWLAnnotation> result = new ArrayList<>(project.getRootOntology().getAnnotations());
        Collections.sort(result, new OntologyAnnotationsComparator(project));
        BrowserTextMap browserTextMap = BrowserTextMap.build(project.getRenderingManager(), result);
        return new GetOntologyAnnotationsResult(ImmutableList.copyOf(result), browserTextMap);
    }

}

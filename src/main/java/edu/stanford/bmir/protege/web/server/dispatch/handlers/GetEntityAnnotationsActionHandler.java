package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetEntityAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetEntityAnnotationsResult;
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
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetEntityAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetEntityAnnotationsAction, GetEntityAnnotationsResult> {

    private final ValidatorFactory<ReadPermissionValidator> validatorFactory;

    @Inject
    public GetEntityAnnotationsActionHandler(OWLAPIProjectManager projectManager, ValidatorFactory<ReadPermissionValidator> validatorFactory) {
        super(projectManager);
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Class<GetEntityAnnotationsAction> getActionClass() {
        return GetEntityAnnotationsAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(GetEntityAnnotationsAction action, RequestContext requestContext) {
        return validatorFactory.getValidator(action.getProjectId(), requestContext.getUserId());
    }

    @Override
    protected GetEntityAnnotationsResult execute(GetEntityAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
        for(OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(action.getSubject())) {
                result.add(ax.getAnnotation());
            }
        }
        BrowserTextMap browserTextMap = new BrowserTextMap(result, project.getRenderingManager());
        return new GetEntityAnnotationsResult(new ArrayList<>(result), browserTextMap);
    }
}

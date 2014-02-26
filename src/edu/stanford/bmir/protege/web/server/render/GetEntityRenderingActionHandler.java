package edu.stanford.bmir.protege.web.server.render;

import edu.stanford.bmir.protege.web.client.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.dispatch.HasProjectAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import java.util.Collection;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/02/2014
 */
public class GetEntityRenderingActionHandler extends AbstractHasProjectActionHandler<GetEntityRenderingAction, GetEntityRenderingResult> {

    @Override
    public Class<GetEntityRenderingAction> getActionClass() {
        return GetEntityRenderingAction.class;
    }

    @Override
    protected RequestValidator<GetEntityRenderingAction> getAdditionalRequestValidator(GetEntityRenderingAction action,
                                                                                       RequestContext requestContext) {
        return null;
    }

    @Override
    protected GetEntityRenderingResult execute(GetEntityRenderingAction action,
                                               final OWLAPIProject project,
                                               ExecutionContext executionContext) {
        OWLEntity entity = action.getEntity();
        HasGetFrameRendering renderer = project.getRenderingManager();
        return new GetEntityRenderingResult(renderer.getFrameRendering(entity));
    }
}

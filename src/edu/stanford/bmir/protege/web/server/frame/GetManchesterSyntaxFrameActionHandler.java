package edu.stanford.bmir.protege.web.server.frame;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.EscapingShortFormProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameAction;
import edu.stanford.bmir.protege.web.shared.frame.GetManchesterSyntaxFrameResult;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntax;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxFrameRenderer;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.RenderingDirector;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.SectionMap;

import java.io.StringWriter;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class GetManchesterSyntaxFrameActionHandler extends AbstractHasProjectActionHandler<GetManchesterSyntaxFrameAction, GetManchesterSyntaxFrameResult> {
    @Override
    protected RequestValidator<GetManchesterSyntaxFrameAction> getAdditionalRequestValidator(GetManchesterSyntaxFrameAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetManchesterSyntaxFrameResult execute(GetManchesterSyntaxFrameAction action, OWLAPIProject project, ExecutionContext executionContext) {
        StringWriter writer = new StringWriter();
        OWLOntology rootOntology = project.getRootOntology();
        EscapingShortFormProvider entityShortFormProvider = new EscapingShortFormProvider(project.getRenderingManager().getShortFormProvider());
        ManchesterOWLSyntaxFrameRenderer frameRenderer = new ManchesterOWLSyntaxFrameRenderer(rootOntology.getImportsClosure(), writer, entityShortFormProvider);
        frameRenderer.setOntologyIRIShortFormProvider(project.getRenderingManager().getOntologyIRIShortFormProvider());
        frameRenderer.setRenderExtensions(true);
        frameRenderer.setUseTabbing(true);
        frameRenderer.setUseWrapping(true);
        frameRenderer.writeFrame(action.getSubject());
        return new GetManchesterSyntaxFrameResult(writer.getBuffer().toString());
    }

    @Override
    public Class<GetManchesterSyntaxFrameAction> getActionClass() {
        return GetManchesterSyntaxFrameAction.class;
    }
}

package edu.stanford.bmir.protege.web.server.dispatch.handlers;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class AbstractGetEntityFrameHandler { //<A extends Action & HasSubject<E>, L extends LabelledFrame<F>, F extends EntityFrame<E>, E extends OWLEntity> extends ProjectSpecificActionHandler<A, GetObjectResult<L>> {

//    @Override
//    public RequestValidator getRequestValidator(A action, RequestContext requestContext) {
//        return new UserHasProjectReadPermissionValidator();
//    }
//
//
//    @Override
//    protected GetObjectResult<L> execute(OWLAPIProject project, A action, ExecutionContext executionContext) {
//        E subject = action.getSubject();
//        RenderingManager rm = project.getRenderingManager();
//        String browserText = rm.getBrowserText(subject);
//        EntityFrameTranslator<F, E> translator = getEntityFrameTranslator();
//        final F frame = translator.getFrame(subject, project.getRootOntology());
//        BrowserTextMap browserTextMap = new BrowserTextMap(frame, rm);
//        L labelledFrame = createLabelledFrame(browserText, frame);
//        return new GetRenderableObjectResult<L>(labelledFrame, browserTextMap);
//    }
//
//    protected abstract EntityFrameTranslator<F, E> getEntityFrameTranslator();
//
//    protected abstract L createLabelledFrame(String displayName, F frame);
}

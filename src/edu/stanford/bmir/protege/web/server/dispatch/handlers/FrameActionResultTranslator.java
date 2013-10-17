package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.frame.FrameTranslator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class FrameActionResultTranslator<F extends EntityFrame<E>, E extends OWLEntity> {

    private OWLAPIProject project;

    private FrameTranslator<F, E> translator;

    private E subject;

    public FrameActionResultTranslator(E subject, OWLAPIProject project, FrameTranslator<F, E> translator) {
        this.subject = subject;
        this.project = project;
        this.translator = translator;
    }

    public LabelledFrame<F> doIT() {
        RenderingManager rm = project.getRenderingManager();
        String browserText = rm.getBrowserText(subject);
        final F frame = translator.getFrame(subject, project.getRootOntology(), project);
        return new LabelledFrame<F>(browserText, frame);
//        L labelledFrame = createLabelledFrame(browserText, frame);
//        return new GetRenderableObjectResult<L>(labelledFrame, browserTextMap);
    }

}

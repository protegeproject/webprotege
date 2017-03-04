package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.client.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class FrameActionResultTranslator<F extends EntityFrame<E>, E extends OWLEntity> {

    private Project project;

    private FrameTranslator<F, E> translator;

    private E subject;

    public FrameActionResultTranslator(E subject, Project project, FrameTranslator<F, E> translator) {
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

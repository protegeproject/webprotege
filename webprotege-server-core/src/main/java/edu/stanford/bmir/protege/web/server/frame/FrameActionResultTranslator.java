package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.EntityFrame;
import edu.stanford.bmir.protege.web.shared.frame.LabelledFrame;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class FrameActionResultTranslator<F extends EntityFrame<E>, E extends OWLEntityData> {

    @Nonnull
    private final RenderingManager renderingManager;

    private final FrameTranslator<F, E> translator;

    private final E subject;


    public FrameActionResultTranslator(@Nonnull RenderingManager renderingManager,
                                       FrameTranslator<F, E> translator, E subject) {
        this.renderingManager = renderingManager;
        this.translator = translator;
        this.subject = subject;
    }

    public LabelledFrame<F> doIT() {
        String browserText = renderingManager.getShortForm(subject.getEntity());
        final F frame = translator.getFrame(subject);
        return new LabelledFrame<>(browserText, frame);
//        L labelledFrame = createLabelledFrame(browserText, frame);
//        return new GetRenderableObjectResult<L>(labelledFrame, browserTextMap);
    }

}

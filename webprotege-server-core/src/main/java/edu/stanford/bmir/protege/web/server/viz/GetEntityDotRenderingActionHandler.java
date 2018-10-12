package edu.stanford.bmir.protege.web.server.viz;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingAction;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingResult;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.StringWriter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
public class GetEntityDotRenderingActionHandler extends AbstractProjectActionHandler<GetEntityDotRenderingAction, GetEntityDotRenderingResult> {

    @Nonnull
    private final DotRendererFactory rendererFactory;

    @Inject
    public GetEntityDotRenderingActionHandler(@Nonnull AccessManager accessManager,
                                              @Nonnull DotRendererFactory rendererFactory) {
        super(accessManager);
        this.rendererFactory = checkNotNull(rendererFactory);
    }

    @Nonnull
    @Override
    public Class<GetEntityDotRenderingAction> getActionClass() {
        return GetEntityDotRenderingAction.class;
    }

    @Nonnull
    @Override
    public GetEntityDotRenderingResult execute(@Nonnull GetEntityDotRenderingAction action, @Nonnull ExecutionContext executionContext) {
        DotRenderer dotRenderer = rendererFactory.create(action.getEntity());
        StringWriter writer = new StringWriter();
        dotRenderer.render(writer);
        System.out.println("Graph------------------------");
        System.out.println(writer.toString());
        return GetEntityDotRenderingResult.get(writer.toString());
    }
}

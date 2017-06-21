package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesActionHandler extends AbstractHasProjectActionHandler<GetOntologyFramesAction, GetOntologyFramesResult> {


    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Inject
    public GetOntologyFramesActionHandler(@Nonnull AccessManager accessManager,
                                          @Nonnull @RootOntology OWLOntology rootOntology,
                                          @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    public GetOntologyFramesResult execute(GetOntologyFramesAction action, ExecutionContext executionContext) {
        List<OntologyFrame> ontologyFrames = rootOntology.getImportsClosure()
                                                         .stream().map(o -> {
                    PropertyValueList list = new PropertyValueList(
                            o.getAnnotations()
                                    .stream()
                                    .map(annotation -> new PropertyAnnotationValue(
                                            renderingManager.getRendering(annotation.getProperty()),
                                            renderingManager.getRendering(annotation.getValue()),
                                            State.ASSERTED))
                                    .collect(Collectors.toList())
                    );
                    return new OntologyFrame(o.getOntologyID(), list, new WebProtegeOntologyIRIShortFormProvider(
                            rootOntology).getShortForm(o));
                }).collect(Collectors.toList());

        return new GetOntologyFramesResult(ImmutableList.copyOf(ontologyFrames));
    }

    @Override
    public Class<GetOntologyFramesAction> getActionClass() {
        return GetOntologyFramesAction.class;
    }
}

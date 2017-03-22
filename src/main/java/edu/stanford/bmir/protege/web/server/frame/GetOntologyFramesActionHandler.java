package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLOntology;

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


    @Inject
    public GetOntologyFramesActionHandler(ProjectManager projectManager, AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetOntologyFramesResult execute(GetOntologyFramesAction action, Project project, ExecutionContext executionContext) {
        OWLOntology rootOntology = project.getRootOntology();
        List<OntologyFrame> ontologyFrames = rootOntology.getImportsClosure()
                .stream().map(o -> {
                    PropertyValueList list = new PropertyValueList(
                            o.getAnnotations()
                                    .stream()
                                    .map(annotation -> new PropertyAnnotationValue(
                                            project.getRenderingManager().getRendering(annotation.getProperty()),
                                            project.getRenderingManager().getRendering(annotation.getValue()),
                                            PropertyValueState.ASSERTED))
                                    .collect(Collectors.toList())
                    );
                    return new OntologyFrame(o.getOntologyID(), list, new WebProtegeOntologyIRIShortFormProvider(rootOntology).getShortForm(o));
                }).collect(Collectors.toList());

        return new GetOntologyFramesResult(ImmutableList.copyOf(ontologyFrames));
    }

    @Override
    public Class<GetOntologyFramesAction> getActionClass() {
        return GetOntologyFramesAction.class;
    }
}

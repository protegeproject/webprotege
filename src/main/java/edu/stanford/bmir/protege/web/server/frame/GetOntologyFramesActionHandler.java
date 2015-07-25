package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.comparator.OntologyAnnotationsComparator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.shortform.WebProtegeOntologyIRIShortFormProvider;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.OntologyIRIShortFormProvider;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25/07/15
 */
public class GetOntologyFramesActionHandler extends AbstractHasProjectActionHandler<GetOntologyFramesAction, GetOntologyFramesResult> {

    @Inject
    public GetOntologyFramesActionHandler(OWLAPIProjectManager projectManager) {
        super(projectManager);
    }

    @Override
    protected RequestValidator<GetOntologyFramesAction> getAdditionalRequestValidator(GetOntologyFramesAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected GetOntologyFramesResult execute(GetOntologyFramesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        OWLOntology rootOntology = project.getRootOntology();
        List<OntologyFrame> ontologyFrames = rootOntology.getImportsClosure()
                .stream().map(o -> {
                    PropertyValueList list = new PropertyValueList(
                            o.getAnnotations()
                                    .stream()
                                    .sorted(new OntologyAnnotationsComparator(project))
                                    .map(annotation -> new PropertyAnnotationValue(annotation.getProperty(), annotation.getValue(), PropertyValueState.ASSERTED))
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

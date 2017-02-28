package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetOntologyAnnotationsResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.comparator.OntologyAnnotationsComparator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetOntologyAnnotationsAction, GetOntologyAnnotationsResult> {

    @Inject
    public GetOntologyAnnotationsActionHandler(OWLAPIProjectManager projectManager,
                                               AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetOntologyAnnotationsAction> getActionClass() {
        return GetOntologyAnnotationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetOntologyAnnotationsResult execute(GetOntologyAnnotationsAction action, OWLAPIProject project, ExecutionContext executionContext) {
        List<OWLAnnotation> result = new ArrayList<>(project.getRootOntology().getAnnotations());
        Collections.sort(result, new OntologyAnnotationsComparator(project));
        BrowserTextMap browserTextMap = BrowserTextMap.build(project.getRenderingManager(), result);
        return new GetOntologyAnnotationsResult(ImmutableList.copyOf(result), browserTextMap);
    }

}

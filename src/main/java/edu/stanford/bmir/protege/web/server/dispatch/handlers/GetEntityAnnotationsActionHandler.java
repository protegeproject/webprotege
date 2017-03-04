package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetEntityAnnotationsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GetEntityAnnotationsResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.VIEW_PROJECT;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetEntityAnnotationsActionHandler extends AbstractHasProjectActionHandler<GetEntityAnnotationsAction, GetEntityAnnotationsResult> {

    @Inject
    public GetEntityAnnotationsActionHandler(ProjectManager projectManager,
                                             AccessManager accessManager) {
        super(projectManager, accessManager);
    }

    @Override
    public Class<GetEntityAnnotationsAction> getActionClass() {
        return GetEntityAnnotationsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return VIEW_PROJECT;
    }

    @Override
    protected GetEntityAnnotationsResult execute(GetEntityAnnotationsAction action, Project project, ExecutionContext executionContext) {
        Set<OWLAnnotation> result = new HashSet<OWLAnnotation>();
        for(OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(action.getSubject())) {
                result.add(ax.getAnnotation());
            }
        }
        BrowserTextMap browserTextMap = new BrowserTextMap(result, project.getRenderingManager());
        return new GetEntityAnnotationsResult(new ArrayList<>(result), browserTextMap);
    }
}

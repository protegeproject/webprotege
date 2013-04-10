package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesAction extends AbstractHasProjectAction<CreateClassesResult> {

    private OWLClass superClass;

    private Set<String> browserTexts;


    private CreateClassesAction() {
    }

    public CreateClassesAction(ProjectId projectId, OWLClass superClass, Set<String> browserTexts) {
        super(projectId);
        this.superClass = superClass;
        this.browserTexts = new HashSet<String>(browserTexts);
    }

    public OWLClass getSuperClass() {
        return superClass;
    }

    public Set<String> getBrowserTexts() {
        return new HashSet<String>(browserTexts);
    }


}

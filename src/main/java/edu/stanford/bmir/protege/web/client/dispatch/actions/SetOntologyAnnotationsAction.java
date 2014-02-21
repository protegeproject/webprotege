package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
public class SetOntologyAnnotationsAction extends AbstractHasProjectAction<SetOntologyAnnotationsResult> {

    private Set<OWLAnnotation> fromAnnotations;

    private Set<OWLAnnotation> toAnnotations;

    /**
     * For serialization purposes only
     */
    private SetOntologyAnnotationsAction() {
    }

    public SetOntologyAnnotationsAction(ProjectId projectId, Set<OWLAnnotation> fromAnnotations, Set<OWLAnnotation> toAnnotations) {
        super(projectId);
        this.fromAnnotations = new HashSet<OWLAnnotation>(fromAnnotations);
        this.toAnnotations = new HashSet<OWLAnnotation>(toAnnotations);
    }

    public Set<OWLAnnotation> getFromAnnotations() {
        return new HashSet<OWLAnnotation>(fromAnnotations);
    }

    public Set<OWLAnnotation> getToAnnotations() {
        return new HashSet<OWLAnnotation>(toAnnotations);
    }
}

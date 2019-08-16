package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/08/2013
 */
public class SetOntologyAnnotationsAction extends AbstractHasProjectAction<SetOntologyAnnotationsResult> {

    private OWLOntologyID owlOntologyId;

    private Set<PropertyAnnotationValue> fromAnnotations;

    private Set<PropertyAnnotationValue> toAnnotations;

    @GwtSerializationConstructor
    private SetOntologyAnnotationsAction() {
    }

    public SetOntologyAnnotationsAction(ProjectId projectId,
                                        OWLOntologyID ontologyID,
                                        Set<PropertyAnnotationValue> fromAnnotations, Set<PropertyAnnotationValue> toAnnotations) {
        super(projectId);
        this.owlOntologyId = checkNotNull(ontologyID);
        this.fromAnnotations = new HashSet<>(fromAnnotations);
        this.toAnnotations = new HashSet<>(toAnnotations);
    }

    public OWLOntologyID getOntologyId() {
        return owlOntologyId;
    }

    public Set<PropertyAnnotationValue> getFromAnnotations() {
        return new HashSet<>(fromAnnotations);
    }

    public Set<PropertyAnnotationValue> getToAnnotations() {
        return new HashSet<>(toAnnotations);
    }
}

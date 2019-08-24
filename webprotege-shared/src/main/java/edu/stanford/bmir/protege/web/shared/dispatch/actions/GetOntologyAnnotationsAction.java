package edu.stanford.bmir.protege.web.shared.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class GetOntologyAnnotationsAction extends AbstractHasProjectAction<GetOntologyAnnotationsResult> {

    private OWLOntologyID ontologyId;

    private GetOntologyAnnotationsAction() {
    }

    public GetOntologyAnnotationsAction(@Nonnull ProjectId projectId,
                                        @Nonnull Optional<OWLOntologyID> ontologyId) {
        super(projectId);
        this.ontologyId = checkNotNull(ontologyId).orElse(null);
    }

    @Nonnull
    public Optional<OWLOntologyID> getOntologyId() {
        return Optional.ofNullable(ontologyId);
    }
}

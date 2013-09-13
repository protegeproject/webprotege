package edu.stanford.bmir.protege.web.shared.individualslist;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsAction extends AbstractHasProjectAction<GetIndividualsResult> {

    private OWLClass type;

    /**
     * For serialization purposes only
     */
    private GetIndividualsAction() {
    }

    public GetIndividualsAction(ProjectId projectId) {
        this(projectId, DataFactory.get().getOWLThing());
    }

    public GetIndividualsAction(ProjectId projectId, OWLClass type) {
        super(projectId);
        this.type = type;
    }

    /**
     * Gets the type of the requested individuals.
     * @return The requested type.  This could be owl:Thing.  Not {@code null}.
     */
    public OWLClass getType() {
        return type;
    }
}

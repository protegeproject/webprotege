package edu.stanford.bmir.protege.web.shared.individualslist;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsAction extends AbstractHasProjectAction<GetIndividualsResult> {

    private OWLClass type;

    private PageRequest pageRequest;

    /**
     * For serialization purposes only
     */
    private GetIndividualsAction() {
    }

    /**
     * Specifies that all individuals in the signature of the specified project should be retrieved.
     *
     * @param projectId The projectId.  Not {@code null}.
     * @param range     The optional range for pagination.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public GetIndividualsAction(ProjectId projectId, Optional<PageRequest> range) {
        this(projectId, DataFactory.get().getOWLThing(), range);
    }

    /**
     * Specifies that the individuals of the specified type should be retrieved from the specified project.  The range
     * argument allows optional pagination.
     *
     * @param projectId The projectId.  Not {@code null}.
     * @param type      The asserted type of the individuals.  Not {@code null}.  A type of owl:Thing means all individuals
     *                  in the ontology should be in the result.
     * @param pageRequest     The optional pageRequest for pagination.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public GetIndividualsAction(ProjectId projectId, OWLClass type, Optional<PageRequest> pageRequest) {
        super(projectId);
        this.type = checkNotNull(type);
        checkNotNull(pageRequest);
        if(pageRequest.isPresent()) {
            this.pageRequest = pageRequest.get();
        }
        else {
            this.pageRequest = PageRequest.requestSinglePage();
        }
    }

    /**
     * Gets the type of the requested individuals.
     *
     * @return The requested type.  This could be owl:Thing.  Not {@code null}.
     */
    public OWLClass getType() {
        return type;
    }

    /**
     * Gets the page request
     *
     * @return Gets the page request.
     */
    public PageRequest getPageRequest() {
        return pageRequest;
    }

    @Override
    public int hashCode() {
        return "GetIndividualsAction".hashCode() + type.hashCode() + pageRequest.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof GetIndividualsAction)) {
            return false;
        }
        GetIndividualsAction other = (GetIndividualsAction) o;
        return this.type.equals(other.type) && this.pageRequest.equals(other.pageRequest);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("GetIndividualsAction")
                .add("type", type)
                .addValue(pageRequest).toString();
    }
}

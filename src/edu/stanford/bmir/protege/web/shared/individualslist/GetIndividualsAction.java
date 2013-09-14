package edu.stanford.bmir.protege.web.shared.individualslist;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.pagination.Range;
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

    private Optional<Range> range;

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
    public GetIndividualsAction(ProjectId projectId, Optional<Range> range) {
        this(projectId, DataFactory.get().getOWLThing(), range);
    }

    /**
     * Specifies that the individuals of the specified type should be retrieved from the specified project.  The range
     * argument allows optional pagination.
     *
     * @param projectId The projectId.  Not {@code null}.
     * @param type      The asserted type of the individuals.  Not {@code null}.  A type of owl:Thing means all individuals
     *                  in the ontology should be in the result.
     * @param range     The optional range for pagination.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public GetIndividualsAction(ProjectId projectId, OWLClass type, Optional<Range> range) {
        super(projectId);
        this.type = checkNotNull(type);
        this.range = checkNotNull(range);
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
     * Gets the range for pagination purposes.
     *
     * @return The pagination Range.  An absent value indicates that no pagination should be performed.  Not {@code null}.
     */
    public Optional<Range> getRange() {
        return range;
    }

    @Override
    public int hashCode() {
        return "GetIndividualsAction".hashCode() + type.hashCode() + range.hashCode();
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
        return this.type.equals(other.type) && this.range.equals(other.range);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("GetIndividualsAction")
                .add("type", type)
                .add("range", range).toString();
    }
}

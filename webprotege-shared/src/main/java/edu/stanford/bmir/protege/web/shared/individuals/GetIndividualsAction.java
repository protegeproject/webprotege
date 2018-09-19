package edu.stanford.bmir.protege.web.shared.individuals;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/09/2013
 */
public class GetIndividualsAction extends AbstractHasProjectAction<GetIndividualsResult> {

    @Nullable
    private OWLClass type;

    @Nullable
    private PageRequest pageRequest;

    private String searchString;

    private InstanceRetrievalMode instanceRetrievalMode;


    @GwtSerializationConstructor
    private GetIndividualsAction() {
    }

    /**
     * Specifies that the individuals of the specified type should be retrieved from the specified project.  The range
     * argument allows optional pagination.
     *
     * @param projectId The projectId.  Not {@code null}.
     * @param type      The asserted type of the individuals.  Not {@code null}.  A type of owl:Thing means all individuals
     *                  in the ontology should be in the result.
     * @param filterString A string that can be used to filter results.  Can be empty to indicate
     *                    no filtering (match everything).  Individuals with browser text containing
     *                    the specified filter string will be included in the result.
     * @param pageRequest     The optional pageRequest for pagination.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public GetIndividualsAction(@Nonnull ProjectId projectId,
                                @Nonnull Optional<OWLClass> type,
                                @Nonnull String filterString,
                                @Nonnull InstanceRetrievalMode instanceRetrievalMode,
                                @Nonnull Optional<PageRequest> pageRequest) {
        super(projectId);
        this.type = checkNotNull(type).orElse(null);
        this.searchString = checkNotNull(filterString);
        this.instanceRetrievalMode = checkNotNull(instanceRetrievalMode);
        this.pageRequest = pageRequest.orElse(null);
    }

    /**
     * Gets the type of the requested individuals.
     *
     * @return The requested type.  This could be owl:Thing.  Not {@code null}.
     */
    @Nonnull
    public Optional<OWLClass> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Gets the search string.
     * @return The search string.  An empty string matches all results.
     */
    @Nonnull
    public String getFilterString() {
        return searchString;
    }

    public InstanceRetrievalMode getInstanceRetrievalMode() {
        return instanceRetrievalMode;
    }

    /**
     * Gets the page request
     *
     * @return Gets the page request.
     */
    public Optional<PageRequest> getPageRequest() {
        return Optional.ofNullable(pageRequest);
    }

    @Override
    public int hashCode() {
        return "GetIndividualsAction".hashCode() + Objects.hashCode(this.type) + Objects.hashCode(pageRequest);
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
        return Objects.equals(this.type, other.type) && Objects.equals(this.pageRequest, other.pageRequest);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetIndividualsAction")
                          .add("type", type)
                          .addValue(pageRequest).toString();
    }
}

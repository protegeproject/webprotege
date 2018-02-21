package edu.stanford.bmir.protege.web.shared.obo;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class GetOboTermIdAction implements ProjectAction<GetOboTermIdResult> {

    private ProjectId projectId;

    private OWLEntity term;


    @GwtSerializationConstructor
    private GetOboTermIdAction() {
    }

    public GetOboTermIdAction(@Nonnull ProjectId projectId,
                              @Nonnull OWLEntity term) {
        this.projectId = checkNotNull(projectId);
        this.term = checkNotNull(term);
    }

    public static GetOboTermIdAction getOboTermId(@Nonnull ProjectId projectId,
                                                  @Nonnull OWLEntity term) {
        return new GetOboTermIdAction(projectId, term);
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public OWLEntity getTerm() {
        return term;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, term);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetOboTermIdAction)) {
            return false;
        }
        GetOboTermIdAction other = (GetOboTermIdAction) obj;
        return this.projectId.equals(other.projectId)
                && this.term.equals(other.term);
    }


    @Override
    public String toString() {
        return toStringHelper("GetOboTermIdAction")
                .addValue(projectId)
                .addValue(term)
                .toString();
    }
}

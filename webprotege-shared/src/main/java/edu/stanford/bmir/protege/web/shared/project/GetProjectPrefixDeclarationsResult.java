package edu.stanford.bmir.protege.web.shared.project;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Feb 2018
 */
public class GetProjectPrefixDeclarationsResult implements Result {

    private ProjectId projectId;

    private List<PrefixDeclaration> prefixDeclarations;

    public GetProjectPrefixDeclarationsResult(ProjectId projectId, List<PrefixDeclaration> prefixDeclarations) {
        this.projectId = checkNotNull(projectId);
        this.prefixDeclarations = new ArrayList<>(checkNotNull(prefixDeclarations));
    }

    @GwtSerializationConstructor
    private GetProjectPrefixDeclarationsResult() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public List<PrefixDeclaration> getPrefixDeclarations() {
        return new ArrayList<>(prefixDeclarations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, prefixDeclarations);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectPrefixDeclarationsResult)) {
            return false;
        }
        GetProjectPrefixDeclarationsResult other = (GetProjectPrefixDeclarationsResult) obj;
        return this.projectId.equals(other.projectId)
                && this.prefixDeclarations.equals(other.prefixDeclarations);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectPrefixDeclarationsResult")
                .addValue(projectId)
                .addValue(prefixDeclarations)
                .toString();
    }
}

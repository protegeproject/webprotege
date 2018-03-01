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
 * 1 Mar 2018
 */
public class SetProjectPrefixDeclarationsResult implements Result {

    private ProjectId projectId;

    private List<PrefixDeclaration> prefixDeclarations;

    public SetProjectPrefixDeclarationsResult(@Nonnull ProjectId projectId,
                                              @Nonnull List<PrefixDeclaration> prefixDeclarations) {
        this.projectId = checkNotNull(projectId);
        this.prefixDeclarations = new ArrayList<>(checkNotNull(prefixDeclarations));
    }

    @GwtSerializationConstructor
    private SetProjectPrefixDeclarationsResult() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets the prefix declarations that were set
     * @return A list of prefix declarations.
     */
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
        if (!(obj instanceof SetProjectPrefixDeclarationsResult)) {
            return false;
        }
        SetProjectPrefixDeclarationsResult other = (SetProjectPrefixDeclarationsResult) obj;
        return this.projectId.equals(other.projectId)
                && this.prefixDeclarations.equals(other.prefixDeclarations);
    }


    @Override
    public String toString() {
        return toStringHelper("SetProjectPrefixDeclarationsResult")
                .addValue(projectId)
                .addValue(prefixDeclarations)
                .toString();
    }
}

package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameAction implements ProjectAction<SetManchesterSyntaxFrameResult>, HasSubject<OWLEntity>, HasFreshEntities {

    private ProjectId projectId;

    private OWLEntity subject;

    private String fromRendering;

    private String toRendering;

    private Set<OWLEntityData> freshEntities;

    private String commitMessage;

    private SetManchesterSyntaxFrameAction() {
    }

    public SetManchesterSyntaxFrameAction(ProjectId projectId, OWLEntity subject, String fromRendering, String toRendering, Set<OWLEntityData> freshEntities, Optional<String> commitMessage) {
        this.projectId = checkNotNull(projectId);
        this.subject = checkNotNull(subject);
        this.fromRendering = checkNotNull(fromRendering);
        this.toRendering = checkNotNull(toRendering);
        this.freshEntities = new HashSet<>(freshEntities);
        this.commitMessage = checkNotNull(commitMessage).orElse(null);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public OWLEntity getSubject() {
        return subject;
    }

    public String getFromRendering() {
        return fromRendering;
    }

    public String getToRendering() {
        return toRendering;
    }

    public Optional<String> getCommitMessage() {
        return Optional.ofNullable(commitMessage);
    }

    public Set<OWLEntityData> getFreshEntities() {
        return new HashSet<OWLEntityData>(freshEntities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, subject, fromRendering, toRendering, freshEntities, commitMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetManchesterSyntaxFrameAction)) {
            return false;
        }
        SetManchesterSyntaxFrameAction other = (SetManchesterSyntaxFrameAction) obj;
        return this.projectId.equals(other.projectId)
                && this.subject.equals(other.subject)
                && this.fromRendering.equals(other.fromRendering)
                && this.toRendering.equals(other.toRendering)
                && this.freshEntities.equals(other.freshEntities)
                && this.getCommitMessage().equals(other.getCommitMessage());
    }


    @Override
    public String toString() {
        return toStringHelper("SetManchesterSyntaxFrameAction")
                .addValue(projectId)
                .add("subject", subject)
                .add("from", fromRendering)
                .add("to", toRendering)
                .add("freshEntities", freshEntities)
                .add("commitMessage", getCommitMessage())
                .toString();
    }
}



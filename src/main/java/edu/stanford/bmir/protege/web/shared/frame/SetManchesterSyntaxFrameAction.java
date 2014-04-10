package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameAction implements Action<SetManchesterSyntaxFrameResult>, HasProjectId, HasSubject<OWLEntity>, HasFreshEntities {

    private ProjectId projectId;

    private OWLEntity subject;

    private String fromRendering;

    private String toRendering;

    private Set<OWLEntityData> freshEntities;

    private String commitMessage;

    private SetManchesterSyntaxFrameAction() {
    }

    public SetManchesterSyntaxFrameAction(ProjectId projectId, OWLEntity subject, String fromRendering, String toRendering, Set<OWLEntityData> freshEntities, Optional<String> commitMessage) {
        this.projectId = projectId;
        this.subject = subject;
        this.fromRendering = fromRendering;
        this.toRendering = toRendering;
        this.freshEntities = new HashSet<OWLEntityData>(freshEntities);
        this.commitMessage = commitMessage.orNull();
    }

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
        return Optional.fromNullable(commitMessage);
    }

    public Set<OWLEntityData> getFreshEntities() {
        return new HashSet<OWLEntityData>(freshEntities);
    }
}

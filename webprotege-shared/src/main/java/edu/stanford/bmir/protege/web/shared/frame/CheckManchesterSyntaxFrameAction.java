package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class CheckManchesterSyntaxFrameAction implements ProjectAction<CheckManchesterSyntaxFrameResult>, HasSubject<OWLEntity>, HasFreshEntities {

    private ProjectId projectId;

    private OWLEntity subject;

    private String from;

    private String to;


    private Set<OWLEntityData> freshEntities;

    private CheckManchesterSyntaxFrameAction() {
    }

    public CheckManchesterSyntaxFrameAction(ProjectId projectId, OWLEntity subject, String from, String to, Collection<OWLEntityData> freshEntities) {
        this.projectId = projectId;
        this.subject = subject;
        this.to = to;
        this.from = from;
        this.freshEntities = new HashSet<OWLEntityData>(freshEntities);
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

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public Set<OWLEntityData> getFreshEntities() {
        return new HashSet<OWLEntityData>(freshEntities);
    }
}

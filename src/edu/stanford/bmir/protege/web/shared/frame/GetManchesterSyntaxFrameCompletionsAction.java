package edu.stanford.bmir.protege.web.shared.frame;

import com.google.common.collect.Sets;
import edu.stanford.bmir.gwtcodemirror.client.EditorPosition;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.HasSubject;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public class GetManchesterSyntaxFrameCompletionsAction implements Action<GetManchesterSyntaxFrameCompletionsResult>, HasProjectId, HasSubject<OWLEntity>, HasFreshEntities {

    private ProjectId projectId;

    private OWLEntity subject;

    private String syntax;

    private EditorPosition fromPos;

    private int from;

    private Set<OWLEntityData> freshEntities;

    private int entityTypeSuggestLimit;

    private GetManchesterSyntaxFrameCompletionsAction() {
    }

    public GetManchesterSyntaxFrameCompletionsAction(ProjectId projectId, OWLEntity subject, EditorPosition fromPos, String syntax, int from, Set<OWLEntityData> freshEntities, int entityTypeSuggestLimit) {
        this.projectId = projectId;
        this.subject = subject;
        this.syntax = syntax;
        this.from = from;
        this.fromPos = fromPos;
        this.freshEntities = Sets.newHashSet(freshEntities);
        this.entityTypeSuggestLimit = entityTypeSuggestLimit;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public OWLEntity getSubject() {
        return subject;
    }

    public String getSyntax() {
        return syntax;
    }

    public int getFrom() {
        return from;
    }

    public EditorPosition getFromPos() {
        return fromPos;
    }

    public int getEntityTypeSuggestLimit() {
        return entityTypeSuggestLimit;
    }

    public Set<OWLEntityData> getFreshEntities() {
        return Sets.newHashSet(freshEntities);
    }
}

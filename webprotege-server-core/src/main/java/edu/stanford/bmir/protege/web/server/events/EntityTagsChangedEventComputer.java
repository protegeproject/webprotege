package edu.stanford.bmir.protege.web.server.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeSubjectProvider;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.tag.EntityTagsChangedEvent;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
@ProjectSingleton
public class EntityTagsChangedEventComputer implements EventTranslator {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final OntologyChangeSubjectProvider provider;

    @Nonnull
    private final TagsManager tagsManager;

    private final SetMultimap<OWLEntity, Tag> beforeChangesTags = HashMultimap.create();

    @Inject
    public EntityTagsChangedEventComputer(@Nonnull ProjectId projectId, @Nonnull OntologyChangeSubjectProvider provider,
                                          @Nonnull TagsManager tagsManager) {
        this.projectId = checkNotNull(projectId);
        this.provider = checkNotNull(provider);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Override
    public void prepareForOntologyChanges(List<OntologyChange> submittedChanges) {
        submittedChanges.forEach(chg -> {
            provider.getChangeSubjects(chg).forEach(entity -> {
                beforeChangesTags.putAll(entity, tagsManager.getTags(entity));
            });
        });
    }

    @Override
    public void translateOntologyChanges(Revision revision, ChangeApplicationResult<?> changes, List<ProjectEvent<?>> projectEventList) {
        changes.getChangeList().forEach(chg -> {
            provider.getChangeSubjects(chg).forEach(entity -> {
                Collection<Tag> tags = tagsManager.getTags(entity);
                if(!tags.equals(beforeChangesTags.get(entity))) {
                    projectEventList.add(new EntityTagsChangedEvent(projectId, entity, tags));
                }
            });
        });
    }
}

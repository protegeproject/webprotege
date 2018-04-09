package edu.stanford.bmir.protege.web.server.events;

import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.shared.event.BrowserTextChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.HasContainsEntityInSignature;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class BrowserTextChangedEventComputer implements EventTranslator {

    private final Map<OWLEntity, String> shortFormMap = Maps.newHashMap();

    private final ProjectId projectId;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    private final HasGetChangeSubjects changeSubjectsProvider;

    private final HasContainsEntityInSignature signature;

    @Inject
    public BrowserTextChangedEventComputer(@Nonnull ProjectId projectId,
                                           @Nonnull DictionaryManager dictionaryManager,
                                           @Nonnull HasGetChangeSubjects changeSubjectsProvider,
                                           @Nonnull HasContainsEntityInSignature signature) {
        this.projectId = checkNotNull(projectId);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.changeSubjectsProvider = checkNotNull(changeSubjectsProvider);
        this.signature = checkNotNull(signature);
    }

    @Override
    public void prepareForOntologyChanges(List<OWLOntologyChange> submittedChanges) {
        submittedChanges.stream()
                        .flatMap(change -> changeSubjectsProvider.getChangeSubjects(change).stream())
                        // The changes might only be extending the signature, so check for this.
                        // If this is the case, then there will not be any existing short form.
                        .filter(signature::containsEntityInSignature)
                        .forEach(entity -> {
                            String shortForm = dictionaryManager.getShortForm(entity);
                            shortFormMap.put(entity, shortForm);
                        });
    }

    @Override
    public void translateOntologyChanges(Revision revision, ChangeApplicationResult<?> changes, List<ProjectEvent<?>> projectEventList) {
        Set<OWLEntity> processedEntities = new HashSet<>();
        changes.getChangeList().stream()
               .flatMap(change -> changeSubjectsProvider.getChangeSubjects(change).stream())
               .distinct()
               .forEach(entity -> {
                   String shortForm = dictionaryManager.getShortForm(entity);
                   String oldShortForm = shortFormMap.get(entity);
                   if(oldShortForm == null || !shortForm.equals(oldShortForm)) {
                       projectEventList.add(new BrowserTextChangedEvent(entity, shortForm, projectId));
                   }
               });
    }
}

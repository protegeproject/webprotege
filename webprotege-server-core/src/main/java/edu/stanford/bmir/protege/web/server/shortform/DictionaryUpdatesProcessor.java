package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.change.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 9 Apr 2018
 *
 * This class is not thread safe.
 */
public class DictionaryUpdatesProcessor {

    @Nonnull
    private final HasGetChangeSubjects changeSubjectsProvider;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Inject
    public DictionaryUpdatesProcessor(@Nonnull HasGetChangeSubjects changeSubjectsProvider,
                                      @Nonnull DictionaryManager dictionaryManager) {
        this.changeSubjectsProvider = checkNotNull(changeSubjectsProvider);
        this.dictionaryManager = checkNotNull(dictionaryManager);
    }

    /**
     * Updates all dictionaries in response to the specified list of (applied) ontology changes.
     */
    public void handleChanges(@Nonnull List<OntologyChange> changes) {
        Stream<OWLEntity> sigStream = changes.stream()
                                             .flatMap(chg -> chg.getSignature().stream());
        // Catches annotations
        Stream<OWLEntity> subjectStream = changes.stream()
                .flatMap(chg -> changeSubjectsProvider.getChangeSubjects(chg).stream());

        Set<OWLEntity> affectedEntities = Stream.concat(sigStream, subjectStream)
                                                .collect(toSet());
        dictionaryManager.update(affectedEntities);
    }

}

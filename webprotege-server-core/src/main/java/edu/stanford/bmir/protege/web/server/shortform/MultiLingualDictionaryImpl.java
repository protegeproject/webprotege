package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
public class MultiLingualDictionaryImpl implements MultiLingualDictionary {

    private static final Logger logger = LoggerFactory.getLogger(MultiLingualDictionaryImpl.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Map<DictionaryLanguage, Dictionary> dictionaries = new HashMap<>();

    @Nonnull
    private DictionaryUpdater dictionaryUpdater;

    @Nonnull
    private DictionaryBuilder dictionaryBuilder;

    @Inject
    public MultiLingualDictionaryImpl(@Nonnull ProjectId projectId,
                                      @Nonnull DictionaryBuilder dictionaryBuilder,
                                      @Nonnull DictionaryUpdater dictionaryUpdater) {
        this.projectId = checkNotNull(projectId);
        this.dictionaryUpdater = checkNotNull(dictionaryUpdater);
        this.dictionaryBuilder = checkNotNull(dictionaryBuilder);
    }

    @Nonnull
    @Override
    public Collection<DictionaryLanguage> getLanguages() {
        return new ArrayList<>(dictionaries.keySet());
    }

    @Nullable
    @Override
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages,
                               @Nullable String defaultShortForm) {
        List<Dictionary> dictionaries = findDictionaries(languages);
        return dictionaries.stream()
                           .map(dictionary -> dictionary.getShortFormOrElse(entity.getIRI(), (i) -> null))
                           .filter(Objects::nonNull)
                           .findFirst()
                           .orElse(defaultShortForm);
    }

    @Override
    public void handleChanges(@Nonnull List<? extends OWLOntologyChange> changes) {
        dictionaries.values().forEach(dictionary -> dictionaryUpdater.update(dictionary, changes));
    }

    @Nonnull
    private List<Dictionary> findDictionaries(@Nonnull List<DictionaryLanguage> languages) {
        final List<Dictionary> result = new ArrayList<>();
        final List<Dictionary> dictionariesToBuild = new ArrayList<>();
        for (DictionaryLanguage language : languages) {
            Dictionary dictionary = dictionaries.get(language);
            if (dictionary == null) {
                dictionary = Dictionary.create(language);
                dictionariesToBuild.add(dictionary);
            }
            result.add(dictionary);
        }
        buildDictionaries(dictionariesToBuild);
        return result;
    }

    private void buildDictionaries(@Nonnull List<Dictionary> dictionaries) {
        logger.info("{} Creating dictionary for <{}> and lang='{}'",
                    projectId,
                    dictionaries);
        Stopwatch stopwatch = Stopwatch.createStarted();
        dictionaryBuilder.buildAll(dictionaries);
        dictionaries.forEach(dictionary -> this.dictionaries.put(dictionary.getLanguage(), dictionary));
        stopwatch.stop();
        logger.info("{} Created dictionary for <{}> and langs='{}' in {} ms",
                    projectId,
                    dictionaries,
                    stopwatch.elapsed(MILLISECONDS));
    }
}

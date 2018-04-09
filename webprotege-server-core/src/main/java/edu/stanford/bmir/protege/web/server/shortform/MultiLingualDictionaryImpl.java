package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
@ProjectSingleton
public class MultiLingualDictionaryImpl implements MultiLingualDictionary {

    private static final Logger logger = LoggerFactory.getLogger(MultiLingualDictionaryImpl.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Map<DictionaryLanguage, Dictionary> dictionaries = new ConcurrentHashMap<>();

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

    @Override
    public void loadLanguages(List<DictionaryLanguage> languages) {
        findDictionaries(languages);
    }

    @Nonnull
    @Override
    public Collection<DictionaryLanguage> getLanguages() {
        return new ArrayList<>(dictionaries.keySet());
    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages,
                               @Nonnull String defaultShortForm) {
        checkNotNull(defaultShortForm);
        List<Dictionary> dictionaries = findDictionaries(languages);
        return dictionaries.stream()
                           .map(dictionary -> dictionary.getShortFormOrElse(entity, (i) -> null))
                           .filter(Objects::nonNull)
                           .findFirst()
                           .orElse(defaultShortForm);
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<String> searchString,
                                                          @Nonnull Set<EntityType<?>> entityTypes,
                                                          @Nonnull List<DictionaryLanguage> languages) {
        if (entityTypes.isEmpty()) {
            return Stream.empty();
        }
        return findDictionaries(languages).stream()
                                          .flatMap(dictionary -> dictionary.getShortFormsContaining(searchString, entityTypes));

    }

    @Override
    public Collection<OWLEntity> getEntities(@Nonnull String shortForm) {
        return dictionaries.values().stream()
                           .flatMap(dictionary -> dictionary.getEntities(shortForm).stream())
                           .collect(toSet());
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
        if (!dictionariesToBuild.isEmpty()) {
            buildDictionaries(dictionariesToBuild);
        }
        return result;
    }

    private void buildDictionaries(@Nonnull List<Dictionary> dictionaries) {
        if (dictionaries.isEmpty()) {
            return;
        }
        List<DictionaryLanguage> langs = dictionaries.stream().map(Dictionary::getLanguage).collect(toList());
        logger.info("{} Building dictionaries for {}",
                    projectId,
                    langs);
        Stopwatch stopwatch = Stopwatch.createStarted();
        dictionaryBuilder.buildAll(dictionaries);
        dictionaries.forEach(dictionary -> this.dictionaries.put(dictionary.getLanguage(), dictionary));
        stopwatch.stop();

        logger.info("{} Built dictionaries for {} in {}",
                    projectId,
                    langs,
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    public void update(@Nonnull Collection<OWLEntity> entities) {
        logger.debug("Updating dictionary entries for {} entities", entities.size());
        dictionaries.values().forEach(dictionary -> dictionaryUpdater.update(dictionary, entities));
    }
}

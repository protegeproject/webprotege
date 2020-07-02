package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
@ProjectSingleton
public class MultiLingualDictionaryImpl implements MultiLingualDictionary, MultilingualDictionaryUpdater {

    private static final Logger logger = LoggerFactory.getLogger(MultiLingualDictionaryImpl.class);

    @Nonnull
    private final Lock writeLock = new ReentrantLock();


    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Map<DictionaryLanguage, Dictionary> dictionaries = new ConcurrentHashMap<>();

    @Nonnull
    private DictionaryUpdater dictionaryUpdater;

    @Nonnull
    private DictionaryBuilder dictionaryBuilder;

    private boolean loaded = false;

    @Inject
    public MultiLingualDictionaryImpl(@Nonnull ProjectId projectId,
                                      @Nonnull DictionaryBuilder dictionaryBuilder,
                                      @Nonnull DictionaryUpdater dictionaryUpdater) {
        this.projectId = checkNotNull(projectId);
        this.dictionaryUpdater = checkNotNull(dictionaryUpdater);
        this.dictionaryBuilder = checkNotNull(dictionaryBuilder);
    }

    /**
     * Causes the specified list of languages to be pre-loaded.
     * @param languages The list of languages that should be loaded.  After loading these languages will
     *                  be available for looking up short forms and entities.
     */
    public synchronized void loadLanguages(@Nonnull List<DictionaryLanguage> languages) {
        if(loaded) {
            return;
        }
        loaded = true;
        // It's suffices to ask for the dictionaries corresponding to these languages
        findDictionaries(languages);
    }

    @Nonnull
    private List<Dictionary> findDictionaries(@Nonnull List<DictionaryLanguage> languages) {
        final var foundDictionaries = new ArrayList<Dictionary>();
        final var dictionariesToBuild = new ArrayList<DictionaryLanguage>();
        for(var language : languages) {
            var dictionary = dictionaries.get(language);
            if(dictionary == null) {
                // At this moment, we need to build a dictionary for this language
                // We might not need to build it in the end if someone builds it before us...
                dictionariesToBuild.add(language);
            }
            else {
                foundDictionaries.add(dictionary);
            }
        }
        if(!dictionariesToBuild.isEmpty()) {
            buildDictionaries(dictionariesToBuild);
            return languages.stream().map(dictionaries::get).collect(toList());
        }
        else {
            return foundDictionaries;
        }

    }

    private void buildDictionaries(@Nonnull List<DictionaryLanguage> dictionaryLanguages) {
        if(dictionaryLanguages.isEmpty()) {
            return;
        }
        try {
            // Prevent others from building the same dictionaries at the same time.  Dictionaries
            // can still be read whilst building is taking place.  Others may make requests for a
            // dictionary that is alread being built but they will not get to build this themselves.
            writeLock.lock();
            var stopwatch = Stopwatch.createStarted();
            logger.info("{} Building dictionaries for {}", projectId, dictionaryLanguages);
            var dictionariesToBuild = dictionaryLanguages
                    .stream()
                    .filter(lang -> !dictionaries.containsKey(lang))
                    .map(Dictionary::create)
                    .collect(toList());

            dictionaryBuilder.buildAll(dictionariesToBuild);
            dictionariesToBuild.forEach(dictionary -> dictionaries.put(dictionary.getLanguage(), dictionary));
            stopwatch.stop();
            logger.info("{} Built dictionaries for {} in {}", projectId, dictionaryLanguages, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } finally {
            writeLock.unlock();
        }

    }

    @Nonnull
    @Override
    public String getShortForm(@Nonnull OWLEntity entity,
                               @Nonnull List<DictionaryLanguage> languages,
                               @Nonnull String defaultShortForm) {
        checkNotNull(defaultShortForm);
        var dictionaries = findDictionaries(languages);
        return dictionaries
                .stream()
                .map(dictionary -> dictionary.getShortForm(entity, ""))
                .filter(shortForm -> !shortForm.isEmpty())
                .findFirst()
                .orElse(defaultShortForm);
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchString,
                                                          @Nonnull Set<EntityType<?>> entityTypes,
                                                          @Nonnull List<DictionaryLanguage> languages) {
        if(entityTypes.isEmpty()) {
            return Stream.empty();
        }
        var dictionaries = findDictionaries(languages);
        return dictionaries
                .stream()
                .flatMap(dictionary -> dictionary.getShortFormsContaining(searchString, entityTypes));

    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntities(@Nonnull String shortForm,
                                         @Nonnull List<DictionaryLanguage> languages) {
        var dictionaries = findDictionaries(languages);
        return dictionaries.stream().flatMap(dictionary -> dictionary.getEntities(shortForm));
    }

    @Override
    public void update(@Nonnull Collection<OWLEntity> entities,
                       @Nonnull List<DictionaryLanguage> languages) {
        logger.debug("Updating dictionary entries for {} entities.", entities.size());
        findAllDictionaries(languages).forEach(dictionary -> dictionaryUpdater.update(dictionary, entities));
    }

    /**
     * Finds all the dictionaries in this multi-lingual dictionary.  The specified list of
     * languages will be guaranteed to exists.
     *
     * @param languages The languages
     * @return The dictionaries
     */
    @Nonnull
    private Stream<Dictionary> findAllDictionaries(@Nonnull List<DictionaryLanguage> languages) {
        // Add any languages that are not present
        findDictionaries(languages);
        return dictionaries.values().stream();
    }

    @Nonnull
    @Override
    public ImmutableMap<DictionaryLanguage, String> getShortForms(@Nonnull OWLEntity entity,
                                                                  @Nonnull List<DictionaryLanguage> languages) {
        var resultBuilder = ImmutableMap.<DictionaryLanguage, String>builder();
        findDictionaries(languages);
        languages.stream()
                 .map(dictionaries::get)
                 .forEach(dictionary -> {
                     var shortForm = dictionary.getShortForm(entity, "");
                     if(!shortForm.isEmpty()) {
                         resultBuilder.put(dictionary.getLanguage(), shortForm);
                     }
                 });
        return resultBuilder.build();
    }
}

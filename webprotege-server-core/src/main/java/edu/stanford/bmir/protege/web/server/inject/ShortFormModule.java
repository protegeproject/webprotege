package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManagerImpl;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLEntityProvider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
@Module
public class ShortFormModule {


    @Provides
    ActiveLanguagesManager provideActiveLanguagesManager(ActiveLanguagesManagerImpl impl) {
        return impl;
    }

    @Provides
    MultiLingualDictionary provideDictionary(MultiLingualDictionaryImpl dictionary, LanguageManager languageManager) {
        // Preload existing languages to avoid delays after loading in the UI
        //        dictionary.loadLanguages(languageManager.getActiveLanguages());
        return dictionary;
    }

    @Provides
    MultilingualDictionaryUpdater provideUpdatableMultilingualDictionary(MultiLingualDictionaryImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    MultiLingualShortFormDictionary provideMultiLingualShortFormDictionary(MultiLingualDictionaryImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    MultiLingualShortFormIndex provideMultiLingualShortFormIndex(MultiLingualDictionaryImpl impl) {
        return impl;
    }

    @Provides
    ShortFormCache provideShortFormCache() {
        return ShortFormCache.create();
    }

    @Provides
    BuiltInShortFormDictionary provideBuiltInShortFormDictionary(ShortFormCache cache, OWLEntityProvider provider) {
        BuiltInShortFormDictionary dictionary = new BuiltInShortFormDictionary(cache, provider);
        dictionary.load();
        return dictionary;
    }


}

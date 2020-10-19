package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManagerImpl;
import edu.stanford.bmir.protege.web.server.shortform.BuiltInShortFormDictionary;
import edu.stanford.bmir.protege.web.server.shortform.Neo4jLuceneModule;
import edu.stanford.bmir.protege.web.server.shortform.ShortFormCache;
import edu.stanford.owl2lpg.client.bind.lang.Neo4jActiveLanguagesManager;
import edu.stanford.owl2lpg.client.read.lang.DictionaryLanguageAccessor;
import edu.stanford.owl2lpg.client.read.lang.DictionaryLanguageAccessorModule;
import org.semanticweb.owlapi.model.OWLEntityProvider;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Module(includes = {
    Neo4jLuceneModule.class,
    DictionaryLanguageAccessorModule.class})
public class Neo4jShortFormModule {

  @Provides
  ActiveLanguagesManager provideActiveLanguagesManager(Neo4jActiveLanguagesManager impl) {
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

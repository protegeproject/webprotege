package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManager;
import edu.stanford.bmir.protege.web.server.lang.ActiveLanguagesManagerImpl;
import edu.stanford.bmir.protege.web.server.shortform.BuiltInShortFormDictionary;
import edu.stanford.bmir.protege.web.server.shortform.Neo4jLuceneModule;
import edu.stanford.bmir.protege.web.server.shortform.ShortFormCache;
import org.semanticweb.owlapi.model.OWLEntityProvider;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Module(includes = Neo4jLuceneModule.class)
public class Neo4jShortFormModule {

  @Provides
  ActiveLanguagesManager provideActiveLanguagesManager(ActiveLanguagesManagerImpl impl) {
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

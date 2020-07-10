package edu.stanford.bmir.protege.web.server.shortform;

import dagger.Component;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-08
 */
@Component(modules = LuceneModule.class)
@ProjectSingleton
public interface LuceneDictionaryComponent {

    MultiLingualDictionary getDictionary();
}

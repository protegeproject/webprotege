package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Apr 2018
 */
@ProjectSingleton
public class LanguageManager {

    private final ActiveLanguagesManager activeLanguagesManager;

    @Inject
    public LanguageManager(@Nonnull ActiveLanguagesManager extractor) {
        this.activeLanguagesManager = checkNotNull(extractor);
    }

    public synchronized ImmutableList<DictionaryLanguage> getLanguages() {
        return activeLanguagesManager.getLanguagesRankedByUsage();
    }
}

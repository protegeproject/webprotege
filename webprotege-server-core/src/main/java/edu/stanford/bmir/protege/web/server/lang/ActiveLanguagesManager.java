package edu.stanford.bmir.protege.web.server.lang;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-05
 */
@ProjectSingleton
public interface ActiveLanguagesManager {

    /**
     * Gets the languages used in the project, ranked in descending order in terms
     * of usage – the most commonly used languages appear first.  This ranking may change
     * as the ontologies in a project are edited.
     */
    @Nonnull
    ImmutableList<DictionaryLanguage> getLanguagesRankedByUsage();

    /**
     * Gets the languages used in the project, ranked in descending order in terms of usage.  The most commonly
     * user languages appear first.
     */
    @Nonnull
    ImmutableList<DictionaryLanguageUsage> getLanguageUsage();

    /**
     * Updates the active languages from the list of applied changes
     *
     * @param changes The changes.
     */
    void handleChanges(@Nonnull List<OntologyChange> changes);
}

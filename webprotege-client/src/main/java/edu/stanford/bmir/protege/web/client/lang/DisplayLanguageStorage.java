package edu.stanford.bmir.protege.web.client.lang;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.gwt.storage.client.Storage;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Aug 2018
 */
@ProjectSingleton
public class DisplayLanguageStorage {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final Storage storage;

    @Inject
    public DisplayLanguageStorage(@Nonnull ProjectId projectId,
                                  @Nonnull Storage storage) {
        this.projectId = checkNotNull(projectId);
        this.storage = checkNotNull(storage);
    }

    public void store(@Nonnull DisplayNameSettings language) {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        storage.setItem(getPrimaryKey(), io.write(language.getPrimaryDisplayNameLanguages()));
        storage.setItem(getSecondaryKey(), io.write(language.getSecondaryDisplayNameLanguages()));
    }

    private String getPrimaryKey() {
        return getKey() + ".primary";
    }

    private String getSecondaryKey() {
        return getKey() + ".secondary";
    }

    @Nonnull
    public DisplayNameSettings get(@Nonnull DisplayNameSettings def) {
        DictionaryLanguageIO io = new DictionaryLanguageIO();
        String primaryVal = storage.getItem(getPrimaryKey());
        ImmutableList<DictionaryLanguage> primary = io.parse(primaryVal);
        String secondaryVal = storage.getItem(getSecondaryKey());
        ImmutableList<DictionaryLanguage> secondary = io.parse(secondaryVal);
        return DisplayNameSettings.get(primary, secondary);
    }

    public void clear() {
        storage.removeItem(getPrimaryKey());
        storage.removeItem(getSecondaryKey());
    }


    private String getKey() {
        return projectId.getId() + ".display.language";
    }

}

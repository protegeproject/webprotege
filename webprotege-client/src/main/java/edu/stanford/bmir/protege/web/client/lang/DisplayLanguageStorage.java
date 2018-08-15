package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.storage.client.Storage;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.IRI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

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

    public void store(@Nonnull DisplayDictionaryLanguage language) {
        storage.removeItem(getPrimaryLangIriKey());
        storage.removeItem(getPrimaryLangTagKey());

        language.getPrimaryLanguage().ifPresent(primaryLang -> {
            Optional.ofNullable(primaryLang.getAnnotationPropertyIri()).ifPresent(iri -> {
                storage.setItem(getPrimaryLangIriKey(), iri.toString());
            });
            storage.setItem(getPrimaryLangTagKey(), primaryLang.getLang());
        });

        language.getSecondaryLanguage().ifPresent(secondaryLang -> {
            Optional.ofNullable(secondaryLang.getAnnotationPropertyIri()).ifPresent(iri -> {
                storage.setItem(getSecondaryLangIriKey(), iri.toString());
            });
            storage.setItem(getSecondaryLangTagKey(), secondaryLang.getLang());
        });
    }

    private String getPrimaryLangTagKey() {
        return getKey() + ".primary.tag";
    }

    private String getPrimaryLangIriKey() {
        return getKey() + ".primary.iri";
    }

    private String getSecondaryLangTagKey() {
        return getKey() + ".secondary.tag";
    }

    private String getSecondaryLangIriKey() {
        return getKey() + ".secondary.iri";
    }

    @Nonnull
    public DisplayDictionaryLanguage get(@Nonnull DisplayDictionaryLanguage def) {
        Optional<DictionaryLanguage> primary = getPrimaryDisplayLanguage();
        Optional<DictionaryLanguage> secondary = getSecondaryDisplayLanguage();
        return DisplayDictionaryLanguage.get(primary, secondary);
    }

    private Optional<DictionaryLanguage> getPrimaryDisplayLanguage() {
        return getPrimaryLanguageIri()
                .map(iri -> DictionaryLanguage.create(IRI.create(iri),
                                                      getPrimaryLanguageTag()));
    }

    private String getPrimaryLanguageTag() {
        return Optional.ofNullable(storage.getItem(getPrimaryLangTagKey())).orElse("");
    }

    private Optional<String> getPrimaryLanguageIri() {
        return Optional.ofNullable(storage.getItem(getPrimaryLangIriKey()));
    }

    private Optional<DictionaryLanguage> getSecondaryDisplayLanguage() {
        return getSecondaryLanguageIri()
                .map(iri -> DictionaryLanguage.create(IRI.create(iri),
                                                      getSecondaryLanguageTag()));
    }

    private String getSecondaryLanguageTag() {
        return Optional.ofNullable(storage.getItem(getSecondaryLangTagKey())).orElse("");
    }

    private Optional<String> getSecondaryLanguageIri() {
        return Optional.ofNullable(storage.getItem(getSecondaryLangIriKey()));
    }

    public void clear() {
        storage.removeItem(getKey());
    }


    private String getKey() {
        return projectId.getId() + ".display.language";
    }

}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Apr 2018
 */
@ProjectSingleton
public interface MultiLingualDictionary {

    @Nonnull
    Collection<DictionaryLanguage> getLanguages();

    void loadLanguages(List<DictionaryLanguage> languages);

    @Nonnull
    String getShortForm(@Nonnull OWLEntity entity,
                        @Nonnull List<DictionaryLanguage> languages,
                        @Nonnull String defaultShortForm);

    @Nonnull
    Collection<String> getShortForms();

    @Nonnull
    Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<String> searchStrings,
                                        @Nonnull List<DictionaryLanguage> languages);

    void handleChanges(@Nonnull List<? extends OWLOntologyChange> changes);

    Collection<OWLEntity> getEntities(@Nonnull String shortForm);
}

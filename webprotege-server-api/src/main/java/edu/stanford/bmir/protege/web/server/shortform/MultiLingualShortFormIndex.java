package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-06
 *
 * Provides an index to entities by their exact short form.
 */
public interface MultiLingualShortFormIndex {
    /**
     * Gets a stream of entities that exactly match the specified short form.
     *
     * @param shortForm The short form.
     * @param languages A list of languages.  All dictionaries that are for the specified languages will be examined.
     */
    @Nonnull
    Stream<OWLEntity> getEntities(@Nonnull String shortForm, @Nonnull List<DictionaryLanguage> languages);
}

package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.queryparser.classic.ParseException;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-07
 */
public interface LuceneIndex {

    @Nonnull
    Stream<EntityShortForms> find(@Nonnull OWLEntity entity,
                                  @Nonnull List<DictionaryLanguage> languages) throws IOException;

    @Nonnull
    Optional<Page<EntityShortFormMatches>> search(@Nonnull List<SearchString> queryString,
                                                  @Nonnull List<DictionaryLanguage> dictionaryLanguages,
                                                  @Nonnull Set<EntityType<?>> entityTypes,
                                                  @Nonnull PageRequest pageRequest) throws IOException, ParseException;

    Stream<OWLEntity> findEntities(String shortForm, List<DictionaryLanguage> languages) throws ParseException, IOException;
}
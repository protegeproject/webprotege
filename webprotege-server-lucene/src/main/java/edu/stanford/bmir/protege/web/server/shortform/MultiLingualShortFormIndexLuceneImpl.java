package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.apache.lucene.queryparser.classic.ParseException;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-15
 */
public class MultiLingualShortFormIndexLuceneImpl implements MultiLingualShortFormIndex {

    private static final Logger logger = LoggerFactory.getLogger(MultiLingualDictionaryLuceneImpl.class);

    @Nonnull
    private final LuceneIndex luceneIndex;


    @Inject
    public MultiLingualShortFormIndexLuceneImpl(@Nonnull LuceneIndex luceneIndex) {
        this.luceneIndex = checkNotNull(luceneIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getEntities(@Nonnull String shortForm, @Nonnull List<DictionaryLanguage> languages) {
        try {
            return luceneIndex.findEntities(shortForm, languages);
        } catch (ParseException | IOException e) {
            logger.error("An error occurred when looking up entities by their short form", e);
            return Stream.empty();
        }
    }
}

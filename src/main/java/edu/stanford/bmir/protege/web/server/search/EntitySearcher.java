package edu.stanford.bmir.protege.web.server.search;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.apache.commons.lang.StringUtils;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.search.SearchField.displayName;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Apr 2017
 * <p>
 * Performs searches against a stream of entities.
 * Instances of this class are not thread safe.
 */
public class EntitySearcher {

    private static final Logger logger = LoggerFactory.getLogger(EntitySearcher.class);

    /**
     * The default limit for the returned results.
     */
    public static final int DEFAULT_LIMIT = 50;

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UserId userId;

    @Nonnull
    private final Supplier<Stream<OWLEntity>> entityStreamSupplier;

    private HasGetRendering renderingSupplier;

    @Nonnull
    private final Set<EntityType<?>> entityTypes;

    @Nonnull
    private final String searchString;

    @Nonnull
    private final String[] searchWords;

    private final Counter searchCounter = new Counter();

    private final Counter matchCounter = new Counter();

    private final List<EntitySearchResult> results = new ArrayList<>();

    private int skip = 0;

    private int limit = DEFAULT_LIMIT;

    private EntitySearcher(@Nonnull ProjectId projectId,
                           @Nonnull UserId userId,
                           @Nonnull Supplier<Stream<OWLEntity>> entityStreamSupplier,
                           @Nonnull HasGetRendering renderingSupplier,
                           @Nonnull Set<EntityType<?>> entityTypes,
                           @Nonnull String searchString) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.entityStreamSupplier = checkNotNull(entityStreamSupplier);
        this.renderingSupplier = checkNotNull(renderingSupplier);
        this.entityTypes = new HashSet<>(checkNotNull(entityTypes));
        this.searchString = checkNotNull(searchString);
        this.searchWords = searchString.split("\\s+");
    }

    public static EntitySearcher get(@Nonnull ProjectId projectId,
                                     @Nonnull UserId userId,
                                     @Nonnull Supplier<Stream<OWLEntity>> entityStreamSupplier,
                                     @Nonnull HasGetRendering renderingSupplier,
                                     @Nonnull Set<EntityType<?>> entityTypes,
                                     @Nonnull String searchString) {
        return new EntitySearcher(projectId,
                                  userId,
                                  entityStreamSupplier,
                                  renderingSupplier,
                                  entityTypes,
                                  searchString);
    }

    /**
     * Gets the skip setting.  The default value is 0.
     *
     * @return The skip setting.
     */
    public int getSkip() {
        return skip;
    }

    /**
     * Sets the skip setting.  This is used to determine the results returned by
     * {@link #getResults()}.
     *
     * @param skip The skip setting, which should be 0 or larger.
     */
    public void setSkip(int skip) {
        checkArgument(skip > -1, "skip must be zero or positive integer");
        this.skip = skip;
    }

    /**
     * Gets the limit setting.  This is used to determine the results returned by
     * {@link #getResults()}.
     *
     * @return The limit setting.  The default value is set to {@link #DEFAULT_LIMIT}.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit setting.  This is used to determine the results returned by
     * {@link #getResults()}.
     *
     * @param limit The limit setting.  This should be a positive integer.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Gets a count of the search results from the last search.
     *
     * @return A count of the search results from the last search.  If a search has not been
     * performed then the returned value will be 0.
     */
    public int getSearchResultsCount() {
        return matchCounter.getCounter();
    }

    /**
     * Gets a subset of the results from the last search as determined by {@link #getSkip()}
     * and {@link #getLimit()}.  Note that changing the skip and limit settings after
     * invoking a search will not have any effect on these results.
     *
     * @return A possible subset of the total results.
     */
    public List<EntitySearchResult> getResults() {
        return new ArrayList<>(results);
    }

    /**
     * Invokes the entity searcher to perform a search.
     */
    public void invoke() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        matchCounter.reset();
        searchCounter.reset();
        results.clear();
        Pattern searchPattern = compileSearchPattern(searchWords);
        entityStreamSupplier.get()
                            .filter(this::isRequiredEntityType)
                            .peek(this::incrementSearchCounter)
                            .map(this::performMatch)
                            .filter(m -> m != null)
                            .peek(this::incrementMatchCounter)
                            .sorted()
                            .skip(skip)
                            .limit(limit)
                            .map(m -> toSearchResult(searchPattern, m))
                            .forEach(results::add);
        logger.info("{} {} Performed entity search for \"{}\".  Found {} matches in {} entities in {} ms.",
                    projectId,
                    userId,
                    searchString,
                    matchCounter.getCounter(),
                    searchCounter.getCounter(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private boolean isRequiredEntityType(OWLEntity e) {
        return entityTypes.contains(e.getEntityType());
    }

    private static Pattern compileSearchPattern(String[] searchWords) {
        StringBuilder searchWordsPattern = new StringBuilder();
        for (int i = 0; i < searchWords.length; i++) {
            searchWordsPattern.append(Pattern.quote(searchWords[i]));
            if (i < searchWords.length - 1) {
                searchWordsPattern.append("|");
            }
        }
        return Pattern.compile(searchWordsPattern.toString(), CASE_INSENSITIVE);
    }

    private void incrementSearchCounter(OWLEntity entity) {
        searchCounter.increment();
    }

    private void incrementMatchCounter(SearchMatch match) {
        matchCounter.increment();
    }

    private static EntitySearchResult toSearchResult(Pattern searchPattern, SearchMatch ren) {
        OWLEntityData ed = ren.getEntityData();
        String rendering = ed.getBrowserText();
        StringBuilder highlighted = new StringBuilder();
        highlightSearchResult(searchPattern, rendering, highlighted);
        if (!ren.isMatchedRendering()) {
            // Matched the IRI remainder
            highlighted.append("<div class=\"searchedIri\">");
            IRI iri = ed.getEntity().getIRI();
            highlightSearchResult(searchPattern, iri.toString(), highlighted);
            highlighted.append("</div>");
        }
        return new EntitySearchResult(ed,
                                      displayName(),
                                      highlighted.toString());
    }

    @Nullable
    private SearchMatch performMatch(@Nonnull OWLEntity e) {
        OWLEntityData rendering = renderingSupplier.getRendering(e);
        boolean matchedRendering = true;
        // All search words must be found
        for (String searchWord : searchWords) {
            if (!StringUtils.containsIgnoreCase(rendering.getBrowserText(),
                                                searchWord)) {
                matchedRendering = false;
                break;
            }
        }
        // If we didn't match the rendering then search the IRI remainder
        boolean matchedIRI = true;
        IRI entityIri = rendering.getEntity().getIRI();
        if (!matchedRendering && entityIri.toString()
                                          .startsWith(Obo2OWLConstants.DEFAULT_IRI_PREFIX)) {
            Optional<String> remainder = entityIri.getRemainder();
            if (remainder.isPresent()) {
                for (String searchWord : searchWords) {
                    if (!StringUtils.containsIgnoreCase(remainder.get(),
                                                        searchWord)) {
                        matchedIRI = false;
                        break;
                    }
                }
            }
        }
        else {
            matchedIRI = false;
        }
        if (matchedRendering || matchedIRI) {
            return new SearchMatch(searchWords, rendering, matchedRendering);
        }
        else {
            return null;
        }
    }

    private static void highlightSearchResult(Pattern searchPattern, String rendering, StringBuilder highlighted) {
        Matcher matcher = searchPattern.matcher(rendering);
        int cur = 0;
        while (matcher.find()) {
            int start = matcher.start();
            highlighted.append(rendering.substring(cur, start));
            highlighted.append("<strong>");
            int end = matcher.end();
            highlighted.append(rendering.substring(start, end));
            highlighted.append("</strong>");
            cur = end;
        }
        if (cur < rendering.length()) {
            highlighted.append(rendering.substring(cur));
        }
    }


    private static class Counter {

        private int counter;

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }

        public void reset() {
            counter = 0;
        }
    }

    private static class SearchMatch implements Comparable<SearchMatch> {


        private final String[] searchWords;

        private final OWLEntityData entityData;

        private final boolean matchedRendering;

        public SearchMatch(@Nonnull String[] searchWords,
                           @Nonnull OWLEntityData entityData,
                           boolean matchedRendering) {
            this.searchWords = searchWords;
            this.entityData = entityData;
            this.matchedRendering = matchedRendering;
        }

        public OWLEntityData getEntityData() {
            return entityData;
        }

        public boolean isMatchedRendering() {
            return matchedRendering;
        }

        @Override
        public int compareTo(@Nonnull SearchMatch other) {
            for (String searchWord : searchWords) {
                int usStart = StringUtils.indexOfIgnoreCase(entityData.getBrowserText(), searchWord);
                int otherStart = StringUtils.indexOfIgnoreCase(other.entityData.getBrowserText(), searchWord);
                if (usStart < otherStart) {
                    return -1;
                }
                else if (otherStart < usStart) {
                    return 1;
                }
            }
            return entityData.getBrowserText().compareToIgnoreCase(other.entityData.getBrowserText());
        }
    }
}

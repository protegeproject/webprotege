package edu.stanford.bmir.protege.web.server.search;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.primitives.ImmutableIntArray;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.mansyntax.render.HasGetRendering;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.search.SearchField.displayName;
import static java.util.Comparator.comparing;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.apache.commons.lang.StringUtils.startsWithIgnoreCase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Apr 2017
 * <p>
 * Performs searches against a stream of entities.
 * Instances of this class are not thread safe.
 */
public class EntitySearcher {

    /**
     * The default limit for the returned results.
     */
    public static final int DEFAULT_LIMIT = 50;

    private static final Pattern OBO_ID_PATTERN = Pattern.compile("([a-z]|[A-Z]+)_([0-9]+)");

    private static final Logger logger = LoggerFactory.getLogger(EntitySearcher.class);

    private final LocalNameExtractor localNameExtractor = new LocalNameExtractor();

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final UserId userId;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Nonnull
    private final Set<EntityType<?>> entityTypes;

    @Nonnull
    private final String searchString;

    @Nonnull
    private final TagsManager tagsManager;

    private final Map<String, Tag> tagsByLabel = new HashMap<>();

    private final Multimap<OWLEntity, Tag> tagsByEntity = HashMultimap.create();

    @Nonnull
    private final ImmutableList<SearchString> searchWords;

    private final Counter searchCounter = new Counter();

    private final Counter matchCounter = new Counter();

    private final List<EntitySearchResult> results = new ArrayList<>();

    private int skip = 0;

    private int limit = DEFAULT_LIMIT;

    @AutoFactory
    public EntitySearcher(@Provided @Nonnull ProjectId projectId,
                          @Provided @Nonnull DictionaryManager dictionaryManager,
                          @Provided @Nonnull LanguageManager languageManager,
                          @Nonnull Set<EntityType<?>> entityTypes,
                          @Provided @Nonnull TagsManager tagsManager,
                          @Nonnull String searchString,
                          @Nonnull UserId userId) {
        this.projectId = checkNotNull(projectId);
        this.userId = checkNotNull(userId);
        this.dictionaryManager = checkNotNull(dictionaryManager);
        this.languageManager = languageManager;
        this.entityTypes = new HashSet<>(checkNotNull(entityTypes));
        this.searchString = checkNotNull(searchString);
        this.searchWords = Stream.of(searchString.split("\\s+"))
                                 .filter(s -> !s.isEmpty())
                                 .map(SearchString::parseSearchString)
                                 .collect(toImmutableList());
        this.tagsManager = checkNotNull(tagsManager);
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

    public void invoke() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        matchCounter.reset();
        searchCounter.reset();
        results.clear();
        tagsByLabel.clear();
        tagsManager.getProjectTags().forEach(tag -> tagsByLabel.put(tag.getLabel().toLowerCase(), tag));
        tagsByEntity.clear();

        Counter filledCounter = new Counter();
        tagsByLabel.entrySet().stream()
                   .filter(e -> e.getKey().startsWith(searchString.toLowerCase()))
                   .map(e -> e.getValue().getTagId())
                   .flatMap(tagId -> tagsManager.getTaggedEntities(tagId).stream())
                   .filter(entity -> entityTypes.contains(entity.getEntityType()))
                   .distinct()
                   .peek(result -> matchCounter.increment())
                   .sorted()
                   .skip(skip)
                   .peek(entity -> filledCounter.increment())
                   .limit(limit)
                   .peek(entity -> tagsByEntity.putAll(entity, tagsManager.getTags(entity)))
                   .map(entity -> {
                       String rendering = dictionaryManager.getShortForm(entity);
                       return toSearchResult(entity, rendering, "", 1, ImmutableIntArray.of(0), MatchType.TAG);
                   })
                   .sorted(comparing(EntitySearchResult::getFieldRendering))
                   .forEach(results::add);
        int limitRemainder = limit - filledCounter.getCounter();
        if (limitRemainder > 0) {
            int skipRemainder = Math.max(skip - filledCounter.getCounter(), 0);
            dictionaryManager.getShortFormsContaining(searchWords,
                                                      entityTypes,
                                                      languageManager.getActiveLanguages())
                             .map(this::performMatch)
                             .filter(Objects::nonNull)
                             .peek(this::incrementMatchCounter)
                             .sorted()
                             .skip(skipRemainder)
                             .limit(limitRemainder)
                             .map(this::toSearchResult)
                             .forEach(results::add);
        }

        logger.info(BROWSING,
                    "{} {} Performed entity search for \"{}\".  Found {} matches in {} ms.",
                    projectId,
                    userId,
                    searchString,
                    matchCounter.getCounter(),
                    stopwatch.elapsed(TimeUnit.MILLISECONDS));

    }

    private void incrementSearchCounter(OWLEntity entity) {
        searchCounter.increment();
    }

    private void incrementMatchCounter(SearchMatch match) {
        matchCounter.increment();
    }

    private EntitySearchResult toSearchResult(SearchMatch match) {
        String displayShortForm;
        OWLEntity entity = match.getEntity();
        if (match.getMatchType() == MatchType.IRI) {
            displayShortForm = dictionaryManager.getShortForm(entity);
        }
        else {
            displayShortForm = match.getShortForm();
        }
        return toSearchResult(entity, displayShortForm, match.getShortForm(), match.getMatchCount(), match.getPositions(), match.getMatchType());
    }

    private EntitySearchResult toSearchResult(OWLEntity entity,
                                              String displayShortForm,
                                              String matchedShortForm,
                                              int matchCount,
                                              ImmutableIntArray positions,
                                              MatchType matchType) {
        StringBuilder highlighted = new StringBuilder();
        if (!displayShortForm.isEmpty() && displayShortForm.equals(matchedShortForm)) {
            highlightSearchResult(displayShortForm, positions, highlighted);
        }
        else {
            highlighted.append(displayShortForm);
        }
        String localName = localNameExtractor.getLocalName(entity.getIRI());
        Matcher matcher = OBO_ID_PATTERN.matcher(localName);
        if (matcher.matches()) {
            highlighted.append("<div style='color: #b4b4b4; margin-left: 5px;'>");
            String oboId = matcher.group(1) + ":" + matcher.group(2);
            if (matchType == MatchType.IRI) {
                highlightSearchResult(oboId, positions, highlighted);
            }
            else {
                highlighted.append(oboId);
            }
            highlighted.append("<div>");
        }
        else if (matchType == MatchType.IRI) {
            // Matched the IRI local name
            highlighted.append("<div style='color: #b4b4b4; margin-left: 5px;'>");
            IRI iri = entity.getIRI();
            highlighted.append(iri.subSequence(0, iri.length() - localName.length()));
            highlightSearchResult(localName, positions, highlighted);
            highlighted.append("</div>");
        }

        if (matchType == MatchType.TAG) {
            for (Tag tag : tagsByEntity.get(entity)) {
                if (startsWithIgnoreCase(tag.getLabel(), searchString)) {
                    highlighted.append("<div class='wp-tag wp-tag--inline-tag' style='color: ")
                               .append(tag.getColor().getHex())
                               .append("; background-color:")
                               .append(tag.getBackgroundColor().getHex()).append(";'>");
                    highlighted.append(tag.getLabel());
                    highlighted.append("</div>");
                }
            }
        }
        return new EntitySearchResult(DataFactory.getOWLEntityData(entity,
                                                                   displayShortForm,
                                                                   dictionaryManager.getShortForms(entity)),
                                      displayName(),
                                      highlighted.toString());
    }


    private void highlightSearchResult(String rendering, ImmutableIntArray matchPositions, StringBuilder highlighted) {
        Map<Integer, String> sortedSearchWords = new TreeMap<>();
        for (int i = 0; i < matchPositions.length(); i++) {
            sortedSearchWords.put(matchPositions.get(i), searchWords.get(i).getSearchString());
        }
        int cur = 0;
        for (Integer i : sortedSearchWords.keySet()) {
            int wordIndex = i;
            String word = sortedSearchWords.get(i);
            int length = word.length();
            int wordEnd = wordIndex + length;
            if (wordIndex > -1 && wordEnd <= rendering.length()) {
                if (cur != wordIndex) {
                    highlighted.append("<span style='white-space: pre;'>");
                    highlighted.append(rendering.substring(cur, wordIndex));
                    highlighted.append("</span>");
                }
                highlighted.append("<strong span style='white-space: pre;'>");
                highlighted.append(rendering.substring(wordIndex, wordEnd));
                highlighted.append("</strong>");
                cur = wordEnd;
            }
        }
        if (cur < rendering.length()) {
            highlighted.append("<span style='white-space: pre;'>");
            highlighted.append(rendering.substring(cur));
            highlighted.append("</span>");
        }
    }


    @Nullable
    private SearchMatch performMatch(@Nonnull ShortFormMatch m) {
        MatchType matchType = null;

        if (m.getLanguage().isAnnotationBased()) {
            matchType = MatchType.RENDERING;
        }

        // If we didn't match the rendering then search the IRI remainder
        IRI entityIri = m.getEntity().getIRI();
        if (matchType == null && !m.getLanguage().isAnnotationBased()) {
            matchType = MatchType.IRI;
        }
        if (matchType != null) {
            return new SearchMatch(searchWords, m, matchType);
        }
        else {
            return null;
        }
    }


    private enum MatchType {
        RENDERING,
        IRI,
        TAG
    }

    private static class SearchMatch implements Comparable<SearchMatch> {

        private static final int BEFORE = -1;

        private static final int AFTER = 1;

        private final static Comparator<SearchMatch> comparator =
                comparing(SearchMatch::getMatchCount).reversed()
                                                     .thenComparing(
                                                             comparing(SearchMatch::getMatchType)
                                                                     .thenComparing(SearchMatch::getShortFormMatch));

        private final ImmutableList<SearchString> searchWords;

        private final ShortFormMatch match;

        private final MatchType matchType;

        public SearchMatch(@Nonnull ImmutableList<SearchString> searchWords,
                           @Nonnull ShortFormMatch match,
                           @Nonnull MatchType matchType) {
            this.searchWords = checkNotNull(searchWords);
            this.match = checkNotNull(match);
            this.matchType = checkNotNull(matchType);
        }

        private ShortFormMatch getShortFormMatch() {
            return match;
        }

        public int getMatchCount() {
            return match.getMatchCount();
        }

        public MatchType getMatchType() {
            return matchType;
        }

        @Override
        public int compareTo(@Nonnull SearchMatch other) {
            return comparator.compare(this, other);
        }

        @Nonnull
        public OWLEntity getEntity() {
            return match.getEntity();
        }

        @Nonnull
        public String getShortForm() {
            return match.getShortForm();
        }

        public ImmutableIntArray getPositions() {
            return match.getMatchPositions();
        }
    }
}

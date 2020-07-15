package edu.stanford.bmir.protege.web.server.search;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.primitives.ImmutableIntArray;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.server.tag.TagsManager;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchResult;
import edu.stanford.bmir.protege.web.shared.search.SearchField;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationPropertyImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static edu.stanford.bmir.protege.web.server.logging.Markers.BROWSING;
import static edu.stanford.bmir.protege.web.shared.search.SearchField.displayName;
import static java.util.Comparator.comparing;
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

    //    private final Counter matchCounter = new Counter();

    private final List<EntitySearchResult> results = new ArrayList<>();

    private int limit = DEFAULT_LIMIT;

    private int matchCount = 0;

    private PageRequest pageRequest = PageRequest.requestFirstPage();

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

    public void setPageRequest(@Nonnull PageRequest pageRequest) {
        this.pageRequest = checkNotNull(pageRequest);
    }

    /**
     * Gets a count of the search results from the last search.
     *
     * @return A count of the search results from the last search.  If a search has not been
     * performed then the returned value will be 0.
     */
    public int getSearchResultsCount() {
        return matchCount;
    }

    /**
     * Gets a subset of the results from the last search as determined by the set page request.
     * Note that changing the page request settings after
     * invoking a search will not have any effect on these results.
     *
     * @return A possible subset of the total results.
     */
    public List<EntitySearchResult> getResults() {
        return new ArrayList<>(results);
    }

    public void invoke() {
        results.clear();

        var entityShortFormMatches = dictionaryManager.getShortFormsContaining(searchWords,
                                                                               entityTypes,
                                                                               languageManager.getActiveLanguages(),
                                                                               pageRequest);

        matchCount = (int) entityShortFormMatches.getTotalElements();

        var r = entityShortFormMatches.getPageElements()
                                      .stream()
                                      .flatMap(match -> match.getShortFormMatches().stream())
                                      .map(shortFormMatch -> {
                                          var entity = shortFormMatch.getEntity();
                                          var entityData = DataFactory.getOWLEntityData(entity,
                                                                                        dictionaryManager.getShortForms(
                                                                                                entity));
                                          var shortForm = shortFormMatch.getShortForm();
                                          var highlighted = new StringBuilder();
                                          highlightSearchResult(shortForm,
                                                                shortFormMatch,
                                                                highlighted,
                                                                shortFormMatch.getLanguage());
                                          return new EntitySearchResult(entityData,
                                                                        SearchField.displayName(),
                                                                        highlighted.toString());

                                      })
                                      .collect(Collectors.toList());
        results.addAll(r);
        //        Stopwatch stopwatch = Stopwatch.createStarted();
        //        matchCounter.reset();
        //        searchCounter.reset();
        //        results.clear();
        //        tagsByLabel.clear();
        //        tagsManager.getProjectTags().forEach(tag -> tagsByLabel.put(tag.getLabel().toLowerCase(), tag));
        //        tagsByEntity.clear();
        //
        //        Counter filledCounter = new Counter();
        //        tagsByLabel.entrySet().stream()
        //                   .filter(e -> e.getKey().startsWith(searchString.toLowerCase()))
        //                   .map(e -> e.getValue().getTagId())
        //                   .flatMap(tagId -> tagsManager.getTaggedEntities(tagId).stream())
        //                   .filter(entity -> entityTypes.contains(entity.getEntityType()))
        //                   .distinct()
        //                   .peek(result -> matchCounter.increment())
        //                   .sorted()
        //                   .skip(skip)
        //                   .peek(entity -> filledCounter.increment())
        //                   .limit(limit)
        //                   .peek(entity -> tagsByEntity.putAll(entity, tagsManager.getTags(entity)))
        //                   .map(entity -> {
        //                       String rendering = dictionaryManager.getShortForm(entity);
        //                       return toSearchResult(entity, rendering, "", ImmutableIntArray.of(0), MatchType.TAG, DictionaryLanguage.localName());
        //                   })
        //                   .sorted(comparing(EntitySearchResult::getFieldRendering))
        //                   .forEach(results::add);
        //        int limitRemainder = limit - filledCounter.getCounter();
        //        if (limitRemainder > 0) {
        //            int skipRemainder = Math.max(skip - filledCounter.getCounter(), 0);
        //            dictionaryManager.getShortFormsContaining(searchWords,
        //                                                      entityTypes,
        //                                                      languageManager.getLanguages(),
        //                                                      PageRequest.requestFirstPage())
        //                             .map(this::performMatch)
        //                             .filter(Objects::nonNull)
        //                             .peek(this::incrementMatchCounter)
        //                             .sorted()
        //                             .skip(skipRemainder)
        //                             .limit(limitRemainder)
        //                             .map(this::toSearchResult)
        //                             .forEach(results::add);
        //        }
        //
        //        logger.info(BROWSING,
        //                    "{} {} Performed entity search for \"{}\".  Found {} matches in {} ms.",
        //                    projectId,
        //                    userId,
        //                    searchString,
        //                    matchCounter.getCounter(),
        //                    stopwatch.elapsed(TimeUnit.MILLISECONDS));

    }

    //    @SuppressWarnings("unused")
    //    private void incrementSearchCounter(OWLEntity entity) {
    //        searchCounter.increment();
    //    }

    //    @SuppressWarnings("unused")
    //    private void incrementMatchCounter(SearchMatch match) {
    //        matchCounter.increment();
    //    }

    //    private EntitySearchResult toSearchResult(SearchMatch match) {
    //        String displayShortForm;
    //        OWLEntity entity = match.getEntity();
    //        if (match.getMatchType() == MatchType.IRI) {
    //            displayShortForm = dictionaryManager.getShortForm(entity);
    //        }
    //        else {
    //            displayShortForm = match.getShortForm();
    //        }
    //        return toSearchResult(entity, displayShortForm, match.getShortForm(), match.getPositions(), match.getMatchType(), match.language);
    //    }

    //    private EntitySearchResult toSearchResult(OWLEntity entity,
    //                                              String displayShortForm,
    //                                              String matchedShortForm,
    //                                              ImmutableIntArray positions,
    //                                              MatchType matchType,
    //                                              DictionaryLanguage dictionaryLanguage) {
    //        StringBuilder highlighted = new StringBuilder();
    //        if (!displayShortForm.isEmpty() && displayShortForm.equals(matchedShortForm)) {
    //            highlightSearchResult(displayShortForm, positions, highlighted, dictionaryLanguage);
    //        }
    //        else {
    //            highlighted.append(displayShortForm);
    //        }
    //        String localName = localNameExtractor.getLocalName(entity.getIRI());
    //        Matcher matcher = OBO_ID_PATTERN.matcher(localName);
    //        if (matcher.matches()) {
    //            highlighted.append("<div style='color: #b4b4b4; margin-left: 5px;'>");
    //            String oboId = matcher.group(1) + ":" + matcher.group(2);
    //            if (matchType == MatchType.IRI) {
    //                highlightSearchResult(oboId, positions, highlighted, dictionaryLanguage);
    //            }
    //            else {
    //                highlighted.append(oboId);
    //            }
    //            highlighted.append("<div>");
    //        }
    //        else if (matchType == MatchType.IRI) {
    //            // Matched the IRI local name
    //            highlighted.append("<div style='color: #b4b4b4; margin-left: 5px;'>");
    //            IRI iri = entity.getIRI();
    //            highlighted.append(iri.subSequence(0, iri.length() - localName.length()));
    //            highlightSearchResult(localName, positions, highlighted, dictionaryLanguage);
    //            highlighted.append("</div>");
    //        }
    //
    //        if (matchType == MatchType.TAG) {
    //            for (Tag tag : tagsByEntity.get(entity)) {
    //                if (startsWithIgnoreCase(tag.getLabel(), searchString)) {
    //                    highlighted.append("<div class='wp-tag wp-tag--inline-tag' style='color: ")
    //                               .append(tag.getColor().getHex())
    //                               .append("; background-color:")
    //                               .append(tag.getBackgroundColor().getHex()).append(";'>");
    //                    highlighted.append(tag.getLabel());
    //                    highlighted.append("</div>");
    //                }
    //            }
    //        }
    //        highlighted.append("</div>");
    //        return new EntitySearchResult(DataFactory.getOWLEntityData(entity,
    //                                                                   dictionaryManager.getShortForms(entity)),
    //                                      displayName(),
    //                                      highlighted.toString());
    //    }


    private void highlightSearchResult(String shortForm,
                                       ShortFormMatch match,
                                       StringBuilder highlighted,
                                       DictionaryLanguage dictionaryLanguage) {
        //        Map<Integer, String> sortedSearchWords = new TreeMap<>();

        int lastEnd = 0;
        for (ShortFormMatchPosition matchPosition : match.getMatchPositions()) {
            int wordStart = matchPosition.getStart();
            int wordEnd = matchPosition.getEnd();
            if (lastEnd != wordStart) {
                // Gap from last search position
                highlighted.append("<span style='white-space: pre;'>");
                highlighted.append(shortForm, lastEnd, wordStart);
                highlighted.append("</span>");
            }
            highlighted.append("<strong span style='white-space: pre;'>");
            highlighted.append(shortForm, wordStart, wordEnd);
            highlighted.append("</strong>");
            lastEnd = wordEnd;
        }
        // Finish to end of shortForm from end of last match position
        if (lastEnd < shortForm.length() - 1) {
            highlighted.append("<span style='white-space: pre;'>");
            highlighted.append(shortForm.substring(lastEnd));
            highlighted.append("</span>");
        }

        if (dictionaryLanguage.isAnnotationBased()) {
            var propRendering = dictionaryManager.getShortForm(new OWLAnnotationPropertyImpl(dictionaryLanguage.getAnnotationPropertyIri()));
            highlighted.append("&nbsp;<span style=\"color: #b4b4b4;\"> ");
            highlighted.append(propRendering);
            String lang = dictionaryLanguage.getLang();
            if (!lang.isEmpty()) {
                highlighted.append(" @");
                highlighted.append(lang);
            }
            highlighted.append("</span>");
        }
    }


    //    @Nullable
    //    private SearchMatch performMatch(@Nonnull ShortFormMatch m) {
    //        MatchType matchType = null;
    //
    //        if (m.getLanguage().isAnnotationBased()) {
    //            matchType = MatchType.RENDERING;
    //        }
    //
    //        // If we didn't match the rendering then search the IRI remainder
    //        if (matchType == null && !m.getLanguage().isAnnotationBased()) {
    //            matchType = MatchType.IRI;
    //        }
    //        if (matchType != null) {
    //            return new SearchMatch(m, matchType, m.getLanguage());
    //        }
    //        else {
    //            return null;
    //        }
    //    }
    //
    //
    private enum MatchType {
        RENDERING, IRI, TAG
    }

    //    private static class SearchMatch implements Comparable<SearchMatch> {
    //
    //        private final static Comparator<SearchMatch> comparator =
    //                comparing(SearchMatch::getMatchCount).reversed()
    //                                                     .thenComparing(
    //                                                             comparing(SearchMatch::getMatchType)
    //                                                                     .thenComparing(SearchMatch::getShortFormMatch));
    //
    //        private final ShortFormMatch match;
    //
    //        private final MatchType matchType;
    //
    //        private final DictionaryLanguage language;
    //
    //        public SearchMatch(@Nonnull ShortFormMatch match,
    //                           @Nonnull MatchType matchType,
    //                           DictionaryLanguage language) {
    //            this.match = checkNotNull(match);
    //            this.matchType = checkNotNull(matchType);
    //            this.language = checkNotNull(language);
    //        }
    //
    //        private ShortFormMatch getShortFormMatch() {
    //            return match;
    //        }
    //
    //        public int getMatchCount() {
    //            return match.getMatchCount();
    //        }
    //
    //        public MatchType getMatchType() {
    //            return matchType;
    //        }
    //
    //        @Override
    //        public int compareTo(@Nonnull SearchMatch other) {
    //            return comparator.compare(this, other);
    //        }
    //
    //        @Nonnull
    //        public OWLEntity getEntity() {
    //            return match.getEntity();
    //        }
    //
    //        @Nonnull
    //        public String getShortForm() {
    //            return match.getShortForm();
    //        }
    //
    //        public ImmutableIntArray getPositions() {
    //            return match.getMatchPositions();
    //        }
    //    }
}

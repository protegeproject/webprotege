package edu.stanford.bmir.protege.web.server.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.server.match.MatcherFactory;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.*;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesActionHandler extends AbstractProjectActionHandler<LookupEntitiesAction, LookupEntitiesResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final PlaceUrl placeUrl;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Nonnull
    private final MatcherFactory matcherFactory;

    @Inject
    public LookupEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull PlaceUrl placeUrl,
                                       @Nonnull EntityNodeRenderer entityNodeRenderer,
                                       @Nonnull DictionaryManager dictionaryManager,
                                       @Nonnull LanguageManager languageManager,
                                       @Nonnull MatcherFactory matcherFactory) {
        super(accessManager);
        this.projectId = projectId;
        this.placeUrl = placeUrl;
        this.entityNodeRenderer = entityNodeRenderer;
        this.dictionaryManager = dictionaryManager;
        this.languageManager = languageManager;
        this.matcherFactory = matcherFactory;
    }

    @Nonnull
    @Override
    public Class<LookupEntitiesAction> getActionClass() {
        return LookupEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(LookupEntitiesAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public LookupEntitiesResult execute(@Nonnull LookupEntitiesAction action,
                                        @Nonnull ExecutionContext executionContext) {
        return new LookupEntitiesResult(lookupEntities(action.getEntityLookupRequest()));
    }


    private List<EntityLookupResult> lookupEntities(final EntityLookupRequest entityLookupRequest) {

        Matcher<OWLEntity> matcher = entityLookupRequest.getEntityMatchCriteria()
                                                        .map(matcherFactory::getMatcher)
                                                        .orElse(entity -> true);
        Set<OWLEntity> addedEntities = new HashSet<>();
        List<SearchString> searchStrings = SearchString.parseMultiWordSearchString(entityLookupRequest.getSearchString());

        var result = dictionaryManager.getShortFormsContaining(searchStrings,
                                                               entityLookupRequest.getSearchedEntityTypes(),
                                                               languageManager.getLanguages(), ImmutableList.of(),
                                                               PageRequest.requestFirstPage());

        List<EntityLookupResult> lookupResults = new ArrayList<>();
        for (var match : result.getPageElements()) {
            var matchedEntity = match.getEntity();
            if (!addedEntities.contains(matchedEntity) && matcher.matches(matchedEntity)) {
                addedEntities.add(matchedEntity);
                for (int i = 0; i < 1 ; i++) {
                    var shortFormMatch = match.getShortFormMatches().get(i);
                    var language = shortFormMatch.getLanguage();
                    var matchPositions = shortFormMatch.getMatchPositions()
                            .stream()
                            .map(p -> SearchResultMatchPosition.get(p.getStart(), p.getEnd()))
                            .collect(toImmutableList());
                    var node = entityNodeRenderer.render(matchedEntity);
                    var matchResult = SearchResultMatch.get(node,
                                                            shortFormMatch.getLanguage(),
                                                            ImmutableMap.of(),
                                                            shortFormMatch.getShortForm(),
                                                            matchPositions);
                    var entityLookupResult = EntityLookupResult.get(language,
                                                                    node,
                                                                    matchResult,
                                                                    placeUrl.getEntityUrl(projectId, matchedEntity));
                    lookupResults.add(entityLookupResult);
                }

            }
        }
        return lookupResults;
    }

    private static class OWLEntityDataMatch implements Comparable<OWLEntityDataMatch> {

        private DictionaryLanguage dictionaryLanguage;

        private OWLEntityData visualEntity;

        private EntityNameMatchResult matchResult;

        private OWLEntityDataMatch(DictionaryLanguage dictionaryLanguage,
                                   OWLEntityData visualEntity,
                                   EntityNameMatchResult matchResult) {
            this.dictionaryLanguage = dictionaryLanguage;
            this.visualEntity = visualEntity;
            this.matchResult = matchResult;
        }

        public DictionaryLanguage getDictionaryLanguage() {
            return dictionaryLanguage;
        }

        public OWLEntityData getEntityData() {
            return visualEntity;
        }

        private EntityNameMatchResult getMatchResult() {
            return matchResult;
        }

        @Override
        public int compareTo(@Nonnull OWLEntityDataMatch owlEntityDataMatch) {
            int diff = this.matchResult.compareTo(owlEntityDataMatch.matchResult);
            if (diff != 0) {
                return diff;
            }
            return visualEntity.compareToIgnorePrefixNames(owlEntityDataMatch.getEntityData());
        }
    }

}

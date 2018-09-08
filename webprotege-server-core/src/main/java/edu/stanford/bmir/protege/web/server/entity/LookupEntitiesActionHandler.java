package edu.stanford.bmir.protege.web.server.entity;

import com.google.common.primitives.ImmutableIntArray;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.lang.LanguageManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.Dictionary;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.SearchString;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchType;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatcher;
import edu.stanford.bmir.protege.web.shared.search.PrefixNameMatchType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
    private final RenderingManager renderingManager;

    @Nonnull
    private final EntityNodeRenderer entityNodeRenderer;

    @Nonnull
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public LookupEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull PlaceUrl placeUrl,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull EntityNodeRenderer entityNodeRenderer,
                                       @Nonnull DictionaryManager dictionaryManager,
                                       @Nonnull LanguageManager languageManager) {
        super(accessManager);
        this.projectId = projectId;
        this.placeUrl = placeUrl;
        this.renderingManager = renderingManager;
        this.entityNodeRenderer = entityNodeRenderer;
        this.dictionaryManager = dictionaryManager;
        this.languageManager = languageManager;
    }

    @Nonnull
    @Override
    public Class<LookupEntitiesAction> getActionClass() {
        return LookupEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public LookupEntitiesResult execute(@Nonnull LookupEntitiesAction action, @Nonnull ExecutionContext executionContext) {
        return new LookupEntitiesResult(lookupEntities(action.getEntityLookupRequest()));
    }


    private List<EntityLookupResult> lookupEntities(final EntityLookupRequest entityLookupRequest) {
        EntityNameMatcher matcher = new EntityNameMatcher(entityLookupRequest.getSearchString());
        Set<OWLEntity> addedEntities = new HashSet<>();
        List<SearchString> searchStrings = SearchString.parseMultiWordSearchString(entityLookupRequest.getSearchString());
        return dictionaryManager.getShortFormsContaining(searchStrings,
                                                         entityLookupRequest.getSearchedEntityTypes(),
                                                         languageManager.getLanguages())
                                // This is arbitrary and possibly leads to bad completion results.  We need to
                                // see how things work in practice when users type in enough to get the right
                                // result.
                                .limit(3000)
                                .filter(match -> !addedEntities.contains(match.getEntity()))
                                .peek(match -> addedEntities.add(match.getEntity()))
                                .sorted()
                                .map(match -> {
                                    // TODO: Change this so that all positions are returned in the result
                                    ImmutableIntArray matchPositions = match.getMatchPositions();
                                    int first = 0;
                                    int start = matchPositions.get(first);
                                    int length = searchStrings.get(first).getSearchString().length();
                                    EntityNameMatchResult result = EntityNameMatchResult.get(
                                            start,
                                            start + length,
                                            // We sort things here now, so just make everything a substring match
                                            EntityNameMatchType.SUB_STRING_MATCH,
                                            PrefixNameMatchType.NOT_IN_PREFIX_NAME
                                    );
                                    OWLEntityData ed = DataFactory.getOWLEntityData(match.getEntity(),
                                                                                    match.getShortForm(),
                                                                                    dictionaryManager.getShortForms(match.getEntity()));
                                    return new OWLEntityDataMatch(match.getLanguage(), ed, result);
                                })
                                .limit(entityLookupRequest.getSearchLimit())
                                .map(this::toEntityLookupResult)
                                .collect(toList());

    }

    private EntityLookupResult toEntityLookupResult(OWLEntityDataMatch match) {
        return EntityLookupResult.get(match.getDictionaryLanguage(),
                                      entityNodeRenderer.render(match.getEntityData().getEntity()),
                                      match.getMatchResult(),
                                      placeUrl.getEntityUrl(projectId, match.getEntityData().getEntity()));
    }

    private static class OWLEntityDataMatch implements Comparable<OWLEntityDataMatch> {

        private DictionaryLanguage dictionaryLanguage;

        private OWLEntityData visualEntity;

        private EntityNameMatchResult matchResult;

        private OWLEntityDataMatch(DictionaryLanguage dictionaryLanguage, OWLEntityData visualEntity, EntityNameMatchResult matchResult) {
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

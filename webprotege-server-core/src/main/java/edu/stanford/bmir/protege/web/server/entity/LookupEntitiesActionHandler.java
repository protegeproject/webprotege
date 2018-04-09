package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.server.shortform.LanguageManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatcher;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

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
    private final DictionaryManager dictionaryManager;

    @Nonnull
    private final LanguageManager languageManager;

    @Inject
    public LookupEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull PlaceUrl placeUrl,
                                       @Nonnull RenderingManager renderingManager,
                                       @Nonnull DictionaryManager dictionaryManager,
                                       @Nonnull LanguageManager languageManager) {
        super(accessManager);
        this.projectId = projectId;
        this.placeUrl = placeUrl;
        this.renderingManager = renderingManager;
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
        return dictionaryManager.getShortFormsContaining(Collections.singletonList(entityLookupRequest.getSearchString()),
                                                         entityLookupRequest.getSearchedEntityTypes(),
                                                         languageManager.getLanguages())
                                // This is arbitary and possibly leads to bad completion results
                                .limit(3000)
                                .filter(match -> !addedEntities.contains(match.getEntity()))
                                .peek(match -> addedEntities.add(match.getEntity()))
                                .map(match -> {
                                    String shortForm = match.getShortForm();
                                    Optional<EntityNameMatchResult> result = matcher.findIn(shortForm);
                                    return result.map(entityNameMatchResult -> {
                                        OWLEntity entity = match.getEntity();
                                        OWLEntityData entityData = DataFactory.getOWLEntityData(entity, match.getShortForm());
                                        return new OWLEntityDataMatch(entityData, entityNameMatchResult);
                                    }).orElse(null);
                                })
                                .filter(Objects::nonNull)
                                .sorted()
                                .limit(entityLookupRequest.getSearchLimit())
                                .map(this::toEntityLookupResult)
                                .collect(toList());

    }

    private EntityLookupResult toEntityLookupResult(OWLEntityDataMatch match) {
        return new EntityLookupResult(match.getEntityData(),
                                      match.getMatchResult(),
                                      placeUrl.getEntityUrl(projectId, match.getEntityData().getEntity()));
    }

    private static class OWLEntityDataMatch implements Comparable<OWLEntityDataMatch> {

        private OWLEntityData visualEntity;

        private EntityNameMatchResult matchResult;

        private OWLEntityDataMatch(OWLEntityData visualEntity, EntityNameMatchResult matchResult) {
            this.visualEntity = visualEntity;
            this.matchResult = matchResult;
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

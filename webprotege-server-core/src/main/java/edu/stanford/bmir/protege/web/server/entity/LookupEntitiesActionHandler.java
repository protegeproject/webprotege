package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.app.PlaceUrl;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatcher;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

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

    @Inject
    public LookupEntitiesActionHandler(@Nonnull AccessManager accessManager,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull PlaceUrl placeUrl,
                                       @Nonnull RenderingManager renderingManager) {
        super(accessManager);
        this.projectId = projectId;
        this.placeUrl = placeUrl;
        this.renderingManager = renderingManager;
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
        BidirectionalShortFormProvider sfp = renderingManager.getShortFormProvider();
        List<OWLEntityDataMatch> matches = new ArrayList<>();
        EntityNameMatcher matcher = new EntityNameMatcher(entityLookupRequest.getSearchString());
        Set<OWLEntity> addedEntities = new HashSet<>();
        for (String shortForm : sfp.getShortForms()) {
            Optional<EntityNameMatchResult> result = matcher.findIn(shortForm);
            if (result.isPresent()) {
                Set<OWLEntity> entities = sfp.getEntities(shortForm);
                for (OWLEntity matchingEntity : entities) {
                    if (!addedEntities.contains(matchingEntity)) {
                        Optional<OWLEntityData> match = toOWLEntityData(matchingEntity, entityLookupRequest,
                                                                        renderingManager);
                        if (match.isPresent()) {
                            EntityNameMatchResult resultValue = result.get();
                            matches.add(new OWLEntityDataMatch(match.get(), resultValue));
                            addedEntities.add(matchingEntity);
                        }
                        // BAD
                        if (matches.size() >= 1000) {
                            break;
                        }
                    }
                }
            }
            // BAD
            if (matches.size() >= 10000) {
                break;
            }
        }
        return matches.stream()
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


    private Optional<OWLEntityData> toOWLEntityData(OWLEntity matchingEntity, final EntityLookupRequest entityLookupRequest, final RenderingManager rm) {
        return matchingEntity.accept(new OWLEntityVisitorEx<Optional<OWLEntityData>>() {
            @Nonnull
            public Optional<OWLEntityData> visit(@Nonnull OWLClass cls) {
                if (entityLookupRequest.isSearchType(EntityType.CLASS)) {
                    OWLEntityData data = rm.getRendering(cls);
                    return Optional.of(data);
                }
                else {
                    return Optional.empty();
                }
            }

            @Nonnull
            public Optional<OWLEntityData> visit(@Nonnull OWLObjectProperty property) {
                if (entityLookupRequest.isSearchType(EntityType.OBJECT_PROPERTY)) {
                    OWLEntityData data = rm.getRendering(property);
                    return Optional.of(data);
                }
                else {
                    return Optional.empty();
                }
            }

            @Nonnull
            public Optional<OWLEntityData> visit(@Nonnull OWLDataProperty property) {
                if (entityLookupRequest.isSearchType(EntityType.DATA_PROPERTY)) {
                    OWLEntityData data = rm.getRendering(property);
                    return Optional.of(data);
                }
                else {
                    return Optional.empty();
                }
            }

            @Nonnull
            public Optional<OWLEntityData> visit(@Nonnull OWLNamedIndividual individual) {
                if (entityLookupRequest.isSearchType(EntityType.NAMED_INDIVIDUAL)) {
                    OWLEntityData data = rm.getRendering(individual);
                    return Optional.of(data);
                }
                else {
                    return Optional.empty();
                }
            }

            @Nonnull
            public Optional<OWLEntityData> visit(@Nonnull OWLDatatype datatype) {
                if (entityLookupRequest.isSearchType(EntityType.DATATYPE)) {
                    OWLEntityData data = rm.getRendering(datatype);
                    return Optional.of(data);
                }
                else {
                    return Optional.empty();
                }
            }

            @Nonnull
            public Optional<OWLEntityData> visit(@Nonnull OWLAnnotationProperty property) {
                if (entityLookupRequest.isSearchType(EntityType.ANNOTATION_PROPERTY)) {
                    OWLEntityData data = rm.getRendering(property);
                    return Optional.of(data);
                }
                else {
                    return Optional.empty();
                }
            }
        });
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

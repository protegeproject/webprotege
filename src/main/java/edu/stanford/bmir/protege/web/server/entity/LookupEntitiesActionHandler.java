package edu.stanford.bmir.protege.web.server.entity;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.place.PlaceUrl;
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

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesActionHandler extends AbstractHasProjectActionHandler<LookupEntitiesAction, LookupEntitiesResult> {

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

    @Override
    public Class<LookupEntitiesAction> getActionClass() {
        return LookupEntitiesAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Override
    public LookupEntitiesResult execute(LookupEntitiesAction action, ExecutionContext executionContext) {
        return new LookupEntitiesResult(lookupEntities(action.getEntityLookupRequest()));
    }



    private List<EntityLookupResult> lookupEntities(final EntityLookupRequest entityLookupRequest) {
        BidirectionalShortFormProvider sfp = renderingManager.getShortFormProvider();
        Set<OWLEntityDataMatch> matches = new TreeSet<>();
        EntityNameMatcher matcher = new EntityNameMatcher(entityLookupRequest.getSearchString());
        for(String shortForm : sfp.getShortForms()) {
            java.util.Optional<EntityNameMatchResult> result = matcher.findIn(shortForm);
            if(result.isPresent()) {
                Set<OWLEntity> entities = sfp.getEntities(shortForm);
                for(OWLEntity matchingEntity : entities) {
                    Optional<OWLEntityData> match = toOWLEntityData(matchingEntity, entityLookupRequest,
                                                                    renderingManager);
                    if(match.isPresent()) {
                        EntityNameMatchResult resultValue = result.get();
                        matches.add(new OWLEntityDataMatch(match.get(), resultValue));
                    }
                    // BAD
                    if(matches.size() >= 1000) {
                        break;
                    }
                }
            }
            // BAD
            if(matches.size() >= 1000) {
                break;
            }
        }
        List<EntityLookupResult> result = new ArrayList<>();
        for(OWLEntityDataMatch match : matches) {
            OWLEntityData entityData = match.getEntityData();
            result.add(new EntityLookupResult(entityData,
                                              match.getMatchResult(),
                                              placeUrl.getEntityUrl(projectId, entityData.getEntity())));
        }
        Collections.sort(result);
        if(result.size() >= entityLookupRequest.getSearchLimit()) {
            result = new ArrayList<>(result.subList(0, entityLookupRequest.getSearchLimit()));
        }
        return result;
    }


    private Optional<OWLEntityData> toOWLEntityData(OWLEntity matchingEntity, final EntityLookupRequest entityLookupRequest, final RenderingManager rm) {
        return matchingEntity.accept(new OWLEntityVisitorEx<Optional<OWLEntityData>>() {
                            @Nonnull
                            public Optional<OWLEntityData> visit(@Nonnull OWLClass cls) {
                                if(entityLookupRequest.isSearchType(EntityType.CLASS)) {
                                    String browserText = rm.getBrowserText(cls);
                                    return Optional.of(new OWLClassData(cls, browserText));
                                }
                                else {
                                    return Optional.empty();
                                }
                            }

                            @Nonnull
                            public Optional<OWLEntityData> visit(@Nonnull OWLObjectProperty property) {
                                if(entityLookupRequest.isSearchType(EntityType.OBJECT_PROPERTY)) {
                                    String browserText = rm.getBrowserText(property);
                                    return Optional.of(new OWLObjectPropertyData(property, browserText));
                                }
                                else {
                                    return Optional.empty();
                                }
                            }

                            @Nonnull
                            public Optional<OWLEntityData> visit(@Nonnull OWLDataProperty property) {
                                if(entityLookupRequest.isSearchType(EntityType.DATA_PROPERTY)) {
                                    String browserText = rm.getBrowserText(property);
                                    return Optional.of(new OWLDataPropertyData(property, browserText));
                                }
                                else {
                                    return Optional.empty();
                                }
                            }

                            @Nonnull
                            public Optional<OWLEntityData> visit(@Nonnull OWLNamedIndividual individual) {
                                if(entityLookupRequest.isSearchType(EntityType.NAMED_INDIVIDUAL)) {
                                    String browserText = rm.getBrowserText(individual);
                                    return Optional.of(new OWLNamedIndividualData(individual, browserText));
                                }
                                else {
                                    return Optional.empty();
                                }
                            }

                            @Nonnull
                            public Optional<OWLEntityData> visit(@Nonnull OWLDatatype datatype) {
                                if(entityLookupRequest.isSearchType(EntityType.DATATYPE)) {
                                    String browserText = rm.getBrowserText(datatype);
                                    return Optional.of(new OWLDatatypeData(datatype, browserText));
                                }
                                else {
                                    return Optional.empty();
                                }
                            }

                            @Nonnull
                            public Optional<OWLEntityData> visit(@Nonnull OWLAnnotationProperty property) {
                                if(entityLookupRequest.isSearchType(EntityType.ANNOTATION_PROPERTY)) {
                                    String browserText = rm.getBrowserText(property);
                                    return Optional.of(new OWLAnnotationPropertyData(property, browserText));
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
            if(diff != 0) {
                return diff;
            }
            return visualEntity.compareToIgnorePrefixNames(owlEntityDataMatch.getEntityData());
        }
    }

}

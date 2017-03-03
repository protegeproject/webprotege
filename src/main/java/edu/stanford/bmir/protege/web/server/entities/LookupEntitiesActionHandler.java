package edu.stanford.bmir.protege.web.server.entities;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatchResult;
import edu.stanford.bmir.protege.web.shared.search.EntityNameMatcher;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

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

    @Inject
    public LookupEntitiesActionHandler(ProjectManager projectManager,
                                       AccessManager accessManager) {
        super(projectManager, accessManager);
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
    protected LookupEntitiesResult execute(LookupEntitiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new LookupEntitiesResult(lookupEntities(project, action.getEntityLookupRequest()));
    }



    private List<EntityLookupResult> lookupEntities(final OWLAPIProject project, final EntityLookupRequest entityLookupRequest) {
        final RenderingManager rm = project.getRenderingManager();
        BidirectionalShortFormProvider sfp = rm.getShortFormProvider();
        Set<OWLEntityDataMatch> matches = new TreeSet<>();
        EntityNameMatcher matcher = new EntityNameMatcher(entityLookupRequest.getSearchString());
        for(String shortForm : sfp.getShortForms()) {
            Optional<EntityNameMatchResult> result = matcher.findIn(shortForm);
            if(result.isPresent()) {
                Set<OWLEntity> entities = sfp.getEntities(shortForm);
                for(OWLEntity matchingEntity : entities) {
                    Optional<OWLEntityData> match = toOWLEntityData(matchingEntity, entityLookupRequest, rm);
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
        List<EntityLookupResult> result = new ArrayList<EntityLookupResult>();
        for(OWLEntityDataMatch match : matches) {
            OWLEntityData entityData = match.getEntityData();
            result.add(new EntityLookupResult(entityData, match.getMatchResult()));
        }
        Collections.sort(result);
        if(result.size() >= entityLookupRequest.getSearchLimit()) {
            result = new ArrayList<EntityLookupResult>(result.subList(0, entityLookupRequest.getSearchLimit()));
        }
        return result;
    }


    private Optional<OWLEntityData> toOWLEntityData(OWLEntity matchingEntity, final EntityLookupRequest entityLookupRequest, final RenderingManager rm) {
        return matchingEntity.accept(new OWLEntityVisitorEx<Optional<OWLEntityData>>() {
                            public Optional<OWLEntityData> visit(OWLClass cls) {
                                if(entityLookupRequest.isSearchType(EntityType.CLASS)) {
                                    String browserText = rm.getBrowserText(cls);
                                    return Optional.<OWLEntityData>of(new OWLClassData(cls, browserText));
                                }
                                else {
                                    return Optional.absent();
                                }
                            }

                            public Optional<OWLEntityData> visit(OWLObjectProperty property) {
                                if(entityLookupRequest.isSearchType(EntityType.OBJECT_PROPERTY)) {
                                    String browserText = rm.getBrowserText(property);
                                    return Optional.<OWLEntityData>of(new OWLObjectPropertyData(property, browserText));
                                }
                                else {
                                    return Optional.absent();
                                }
                            }

                            public Optional<OWLEntityData> visit(OWLDataProperty property) {
                                if(entityLookupRequest.isSearchType(EntityType.DATA_PROPERTY)) {
                                    String browserText = rm.getBrowserText(property);
                                    return Optional.<OWLEntityData>of(new OWLDataPropertyData(property, browserText));
                                }
                                else {
                                    return Optional.absent();
                                }
                            }

                            public Optional<OWLEntityData> visit(OWLNamedIndividual individual) {
                                if(entityLookupRequest.isSearchType(EntityType.NAMED_INDIVIDUAL)) {
                                    String browserText = rm.getBrowserText(individual);
                                    return Optional.<OWLEntityData>of(new OWLNamedIndividualData(individual, browserText));
                                }
                                else {
                                    return Optional.absent();
                                }
                            }

                            public Optional<OWLEntityData> visit(OWLDatatype datatype) {
                                if(entityLookupRequest.isSearchType(EntityType.DATATYPE)) {
                                    String browserText = rm.getBrowserText(datatype);
                                    return Optional.<OWLEntityData>of(new OWLDatatypeData(datatype, browserText));
                                }
                                else {
                                    return Optional.absent();
                                }
                            }

                            public Optional<OWLEntityData> visit(OWLAnnotationProperty property) {
                                if(entityLookupRequest.isSearchType(EntityType.ANNOTATION_PROPERTY)) {
                                    String browserText = rm.getBrowserText(property);
                                    return Optional.<OWLEntityData>of(new OWLAnnotationPropertyData(property, browserText));
                                }
                                else {
                                    return Optional.absent();
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
        public int compareTo(OWLEntityDataMatch owlEntityDataMatch) {
            int diff = this.matchResult.compareTo(owlEntityDataMatch.matchResult);
            if(diff != 0) {
                return diff;
            }
            return visualEntity.compareToIgnorePrefixNames(owlEntityDataMatch.getEntityData());
        }
    }

}

package edu.stanford.bmir.protege.web.server.entities;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.search.SearchType;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/11/2013
 */
public class LookupEntitiesActionHandler extends AbstractHasProjectActionHandler<LookupEntitiesAction, LookupEntitiesResult> {

    public static final String WORD_BOUNDARY_PATTERN_START = "\'?\\b";

    public static final String NON_WORD_BOUNDARY_PATTERN_START = "\'?";

    @Override
    public Class<LookupEntitiesAction> getActionClass() {
        return LookupEntitiesAction.class;
    }

    @Override
    protected RequestValidator<LookupEntitiesAction> getAdditionalRequestValidator(LookupEntitiesAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected LookupEntitiesResult execute(LookupEntitiesAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new LookupEntitiesResult(lookupEntities(project, action.getEntityLookupRequest()));
    }



    private List<EntityLookupResult> lookupEntities(final OWLAPIProject project, final EntityLookupRequest entityLookupRequest) {
        final RenderingManager rm = project.getRenderingManager();
        BidirectionalShortFormProvider sfp = rm.getShortFormProvider();

        AtomicReference<String> searchString = new AtomicReference<String>(entityLookupRequest.getSearchString());
        String quotedSearchString = Pattern.quote(searchString.get());
        final int flags = getFlags(entityLookupRequest);
        Pattern wordBoundaryPattern = Pattern.compile(WORD_BOUNDARY_PATTERN_START + quotedSearchString, flags);
        Pattern nonWordBoundaryPattern = Pattern.compile(NON_WORD_BOUNDARY_PATTERN_START + quotedSearchString, flags);
        Set<OWLEntityDataMatch> matches = new TreeSet<OWLEntityDataMatch>();
        for(String shortForm : sfp.getShortForms()) {
            final Matcher matcher;
            Matcher wordBoundaryMatcher = wordBoundaryPattern.matcher(shortForm);
            boolean matched = isMatch(shortForm, wordBoundaryMatcher, entityLookupRequest);
            if(matched) {
                matcher = wordBoundaryMatcher;
            }
            else {
                Matcher nonWordBoundaryMatcher = nonWordBoundaryPattern.matcher(shortForm);
                matched = isMatch(shortForm, nonWordBoundaryMatcher, entityLookupRequest);
                matcher = nonWordBoundaryMatcher;
            }

            if(matched) {
                Set<OWLEntity> entities = sfp.getEntities(shortForm);
                for(OWLEntity matchingEntity : entities) {
                    Optional<OWLEntityData> match = toOWLEntityData(matchingEntity, entityLookupRequest, rm);
                    if(match.isPresent()) {
                        matches.add(new OWLEntityDataMatch(matcher.start(), matcher.end(), match.get()));
                    }
                    if(matches.size() >= 1000) {
                        break;
                    }
                }
            }
            if(matches.size() >= 1000) {
                break;
            }
        }
        List<EntityLookupResult> result = new ArrayList<EntityLookupResult>();
        for(OWLEntityDataMatch visualEntityMatch : matches) {
            OWLEntityData visualEntity = visualEntityMatch.getVisualEntity();
            result.add(new EntityLookupResult(visualEntity, visualEntityMatch.matchIndex, visualEntityMatch.matchEndIndex));
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

    private int getFlags(EntityLookupRequest entityLookupRequest) {
        int flags;
        if(entityLookupRequest.getSearchType().isCaseInsensitive()) {
            flags = Pattern.CASE_INSENSITIVE;
        }
        else {
            flags = 0;
        }
        return flags;
    }

    private boolean isMatch(String shortForm, Matcher matcher, EntityLookupRequest request) {
        if(request.getSearchType() == SearchType.EXACT_MATCH) {
            return matcher.matches();
        }
        else {
            int prefixSeparatorIndex = shortForm.indexOf(':');
            int startFrom;
            if(prefixSeparatorIndex != -1) {
                startFrom = prefixSeparatorIndex;
            }
            else {
                startFrom = 0;
            }


            boolean localNameMatched = matcher.find(startFrom);
            if(!localNameMatched && startFrom != 0) {
                return matcher.find();
            }
            boolean subsequentMatch = localNameMatched;
            while(subsequentMatch) {
                subsequentMatch = matcher.find(matcher.start() + 1);
                if(subsequentMatch) {
                    if(Character.isUpperCase(shortForm.charAt(matcher.start()))) {
                        return true;
                    }
                }
            }
            return localNameMatched && matcher.find(startFrom);
        }
    }


    private static class OWLEntityDataMatch implements Comparable<OWLEntityDataMatch> {

        private int matchIndex;

        private int matchEndIndex;

        private OWLEntityData visualEntity;

        private OWLEntityDataMatch(int matchIndex, int matchEndIndex, OWLEntityData visualEntity) {
            this.matchIndex = matchIndex;
            this.matchEndIndex = matchEndIndex;
            this.visualEntity = visualEntity;
        }

        public OWLEntityData getVisualEntity() {
            return visualEntity;
        }

        public int compareTo(OWLEntityDataMatch o) {
            int diff = matchIndex - o.matchIndex;
            if(diff != 0) {
                return diff;
            }
            String browserText1 = visualEntity.getBrowserText();
            if(browserText1.startsWith("'")) {
                browserText1 = browserText1.substring(1);
            }
            String browserText2 = o.getVisualEntity().getBrowserText();
            if(browserText2.startsWith("'")) {
                browserText2 = browserText2.substring(1);
            }
            return browserText1.compareToIgnoreCase(browserText2);
        }
    }

}

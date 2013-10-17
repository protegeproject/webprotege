package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.EntityLookupService;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceResult;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequestType;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
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
 * Date: 21/05/2012
 */
public class EntityLookupServiceImpl extends WebProtegeRemoteServiceServlet implements EntityLookupService {

    private OWLAPIProject getProject(ProjectId projectId) {
        OWLAPIProjectManager projectManager = OWLAPIProjectManager.getProjectManager();
        return projectManager.getProject(projectId);
    }
    
    
    public List<EntityLookupServiceResult> lookupEntities(final ProjectId projectId, final EntityLookupRequest entityLookupRequest) {
        OWLAPIProject project = getProject(projectId);
        final RenderingManager rm = project.getRenderingManager();
        BidirectionalShortFormProvider sfp = rm.getShortFormProvider();
        
        AtomicReference<String> searchString = new AtomicReference<String>(entityLookupRequest.getSearchString());
        String quotedSearchString = Pattern.quote(searchString.get());
        int flags;
        if(entityLookupRequest.getMatchType().isCaseInsensitive()) {
            flags = Pattern.CASE_INSENSITIVE;
        }
        else {
            flags = 0;
        }
        Pattern wordBoundaryPattern = Pattern.compile("\'?\\b" + quotedSearchString, flags);
        Pattern nonWordBoundaryPattern = Pattern.compile("\'?" + quotedSearchString, flags);
        Set<OWLEntityDataMatch> matches = new TreeSet<OWLEntityDataMatch>();
        for(String shortForm : sfp.getShortForms()) {
            Matcher matcher;
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
                    OWLEntityData match = matchingEntity.accept(new OWLEntityVisitorEx<OWLEntityData>() {
                        public OWLEntityData visit(OWLClass cls) {
                            if(entityLookupRequest.isMatchType(EntityType.CLASS)) {
                                String browserText = rm.getBrowserText(cls);
                                return new OWLClassData(cls, browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public OWLEntityData visit(OWLObjectProperty property) {
                            if(entityLookupRequest.isMatchType(EntityType.OBJECT_PROPERTY)) {
                                String browserText = rm.getBrowserText(property);
                                return new OWLObjectPropertyData(property, browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public OWLEntityData visit(OWLDataProperty property) {
                            if(entityLookupRequest.isMatchType(EntityType.DATA_PROPERTY)) {
                                String browserText = rm.getBrowserText(property);
                                return new OWLDataPropertyData(property, browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public OWLEntityData visit(OWLNamedIndividual individual) {
                            if(entityLookupRequest.isMatchType(EntityType.NAMED_INDIVIDUAL)) {
                                String browserText = rm.getBrowserText(individual);
                                return new OWLNamedIndividualData(individual, browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public OWLEntityData visit(OWLDatatype datatype) {
                            if(entityLookupRequest.isMatchType(EntityType.DATATYPE)) {
                                String browserText = rm.getBrowserText(datatype);
                                return new OWLDatatypeData(datatype, browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public OWLEntityData visit(OWLAnnotationProperty property) {
                            if(entityLookupRequest.isMatchType(EntityType.ANNOTATION_PROPERTY)) {
                                String browserText = rm.getBrowserText(property);
                                return new OWLAnnotationPropertyData(property, browserText);
                            }
                            else {
                                return null;
                            }
                        }
                    });
                    if(match != null) {
                        matches.add(new OWLEntityDataMatch(matcher.start(), matcher.end(), match));
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
        List<EntityLookupServiceResult> result = new ArrayList<EntityLookupServiceResult>();
        for(OWLEntityDataMatch visualEntityMatch : matches) {
            OWLEntityData visualEntity = visualEntityMatch.getVisualEntity();
            result.add(new EntityLookupServiceResult(visualEntity, visualEntityMatch.matchIndex, visualEntityMatch.matchEndIndex));
        }
        Collections.sort(result);
        if(result.size() >= entityLookupRequest.getMatchLimit()) {
            result = new ArrayList<EntityLookupServiceResult>(result.subList(0, entityLookupRequest.getMatchLimit()));
        }
        return result;
    }

    private boolean isMatch(String shortForm, Matcher matcher, EntityLookupRequest request) {
        if(request.getMatchType() == EntityLookupRequestType.EXACT_MATCH) {
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

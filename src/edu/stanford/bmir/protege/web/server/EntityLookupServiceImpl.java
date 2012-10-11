package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.EntityLookupService;
import edu.stanford.bmir.protege.web.client.rpc.EntityLookupServiceResult;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequestEntityMatchType;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.*;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.IRI;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
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
        Pattern pattern = Pattern.compile("\'?" + quotedSearchString, Pattern.CASE_INSENSITIVE);

        Set<VisualEntityMatch> matches = new TreeSet<VisualEntityMatch>();
        for(String shortForm : sfp.getShortForms()) {
            Matcher matcher = pattern.matcher(shortForm);
            if(matcher.find()) {
                Set<OWLEntity> entities = sfp.getEntities(shortForm);
                for(OWLEntity matchingEntity : entities) {
                    VisualEntity<?> match = matchingEntity.accept(new OWLEntityVisitorEx<VisualEntity<?>>() {
                        public VisualEntity<?> visit(OWLClass cls) {
                            if(entityLookupRequest.isMatchType(EntityLookupRequestEntityMatchType.MATCH_CLASSES)) {
                                String browserText = rm.getBrowserText(cls);
                                return new VisualNamedClass(new NamedClass(IRI.create(cls.getIRI().toString())), browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public VisualEntity<?> visit(OWLObjectProperty property) {
                            if(entityLookupRequest.isMatchType(EntityLookupRequestEntityMatchType.MATCH_OBJECT_PROPERTIES)) {
                                String browserText = rm.getBrowserText(property);
                                return new VisualObjectProperty(new ObjectProperty(IRI.create(property.getIRI().toString())), browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public VisualEntity<?> visit(OWLDataProperty property) {
                            if(entityLookupRequest.isMatchType(EntityLookupRequestEntityMatchType.MATCH_DATA_PROPERTIES)) {
                                String browserText = rm.getBrowserText(property);
                                return new VisualDataProperty(new DataProperty(IRI.create(property.getIRI().toString())), browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public VisualEntity<?> visit(OWLNamedIndividual individual) {
                            if(entityLookupRequest.isMatchType(EntityLookupRequestEntityMatchType.MATCH_NAMED_INDIVIDUALS)) {
                                String browserText = rm.getBrowserText(individual);
                                return new VisualNamedIndividual(new NamedIndividual(IRI.create(individual.getIRI().toString())), browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public VisualEntity<?> visit(OWLDatatype datatype) {
                            if(entityLookupRequest.isMatchType(EntityLookupRequestEntityMatchType.MATCH_DATATYPES)) {
                                String browserText = rm.getBrowserText(datatype);
                                return new VisualDatatype(new Datatype(IRI.create(datatype.getIRI().toString())), browserText);
                            }
                            else {
                                return null;
                            }
                        }

                        public VisualEntity<?> visit(OWLAnnotationProperty property) {
                            if(entityLookupRequest.isMatchType(EntityLookupRequestEntityMatchType.MATCH_ANNOTATION_PROPERTIES)) {
                                String browserText = rm.getBrowserText(property);
                                return new VisualAnnotationProperty(new AnnotationProperty(IRI.create(property.getIRI().toString())), browserText);
                            }
                            else {
                                return null;
                            }
                        }
                    });
                    if(match != null) {
                        matches.add(new VisualEntityMatch(matcher.start(), matcher.end(), match));
                    }
                    if(matches.size() >= entityLookupRequest.getMatchLimit()) {
                        break;
                    }
                }
            }
            if(matches.size() >= entityLookupRequest.getMatchLimit()) {
                break;
            }
        }
        List<EntityLookupServiceResult> result = new ArrayList<EntityLookupServiceResult>();
        for(VisualEntityMatch visualEntityMatch : matches) {
            VisualEntity<?> visualEntity = visualEntityMatch.getVisualEntity();
            result.add(new EntityLookupServiceResult(visualEntity, visualEntityMatch.matchIndex, visualEntityMatch.matchEndIndex));
        }
        return result;
    }
    
    private class VisualEntityMatch implements Comparable<VisualEntityMatch> {
        
        private int matchIndex;
        
        private int matchEndIndex;
        
        private VisualEntity<?> visualEntity;

        private VisualEntityMatch(int matchIndex, int matchEndIndex, VisualEntity<?> visualEntity) {
            this.matchIndex = matchIndex;
            this.matchEndIndex = matchEndIndex;
            this.visualEntity = visualEntity;
        }

        public VisualEntity<?> getVisualEntity() {
            return visualEntity;
        }

        public int compareTo(VisualEntityMatch o) {
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

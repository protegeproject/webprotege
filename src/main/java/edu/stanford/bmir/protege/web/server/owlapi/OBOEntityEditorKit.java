package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/05/2012
 */
public class OBOEntityEditorKit extends OWLAPIEntityEditorKit {

    public static final int DEFAULT_ID_LENGTH = 7;
    
    private int idLength = DEFAULT_ID_LENGTH;

    private final OBOEntityShortFormProvider webProtegeShortFormProvider;

    private final OBOEntityCreatorFactory oboEntityCreatorFactory;


    public OBOEntityEditorKit(OWLAPIProject project) {
        super(project);
        webProtegeShortFormProvider = new OBOEntityShortFormProvider(getProject());
        oboEntityCreatorFactory = new OBOEntityCreatorFactory(project, idLength);
    }

    @Override
    public OWLEntityCreatorFactory getEntityCreatorFactory() {
        return oboEntityCreatorFactory;
    }

    @Override
    public ShortFormProvider getShortFormProvider() {
        return webProtegeShortFormProvider;
    }

    @Override
    public boolean isBrowserTextEditable(OWLEntity entity) {
        return true;
    }

    @Override
    public String getEntityBrowserText(OWLEntity entity) {
        return webProtegeShortFormProvider.getShortForm(entity);
    }

    @Override
    public OWLEntityBrowserTextChangeSet setEntityBrowserText(OWLEntity entity, String browserText) {
        return new OWLEntityBrowserTextChangeSet(entity, getEntityBrowserText(entity), browserText);
    }

    @Override
    public List<OWLEntityBrowserTextChangeSet> getChangedEntities(List<OWLOntologyChangeRecord> ontologyChanges) {
        List<OWLEntityBrowserTextChangeSet> result = new ArrayList<OWLEntityBrowserTextChangeSet>();
        for(OWLOntologyChangeRecord record : ontologyChanges) {
            OWLOntologyChangeData info = record.getData();
            BrowserTextChangeDetector detector = new BrowserTextChangeDetector();
            info.accept(detector);
            for(BrowserTextChange change : detector.getBrowserTextChanges()) {
                for(OWLEntity entity : getProject().getRootOntology().getEntitiesInSignature(change.getSubject())) {
                    result.add(new OWLEntityBrowserTextChangeSet(entity, change.getFrom(), change.getTo()));
                }
            }
        }
        return result;
    }

    @Override
    public void dispose() {
    }
    
    
    private static class BrowserTextChange {
        
        private IRI subject;
        
        private String from;
        
        private String to;

        private BrowserTextChange(IRI subject, String from, String to) {
            this.subject = subject;
            this.from = from;
            this.to = to;
        }

        public IRI getSubject() {
            return subject;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
    
    
    private class BrowserTextChangeDetector extends OWLAxiomVisitorAdapter implements OWLOntologyChangeDataVisitor<Object, RuntimeException> {

        // Order is important!
        
        private Map<IRI, String> fromMap = new LinkedHashMap<IRI, String>(4);
        
        private Map<IRI, String> toMap = new LinkedHashMap<IRI, String>(4);
        
        private ChangeType changeType;
        
        public List<BrowserTextChange> getBrowserTextChanges() {
            List<BrowserTextChange> changes = new ArrayList<BrowserTextChange>();
            for(IRI fromIRI : fromMap.keySet()) {
                String fromBrowserText = fromMap.get(fromIRI);
                String toBrowserText = toMap.get(fromIRI);
                changes.add(new BrowserTextChange(fromIRI, fromBrowserText == null ? "" : fromBrowserText, toBrowserText == null ? "" : toBrowserText));
            }
            for(IRI toIRI : toMap.keySet()) {
                if(!fromMap.containsKey(toIRI)) {
                    String fromBrowserText = "";
                    String toBrowserText = toMap.get(toIRI);
                    changes.add(new BrowserTextChange(toIRI, fromBrowserText, toBrowserText == null ? "" : toBrowserText));
                }
            }
            return changes;
        }
        
        public Object visit(AddAxiomData data) throws RuntimeException {
            OWLAxiom axiom = data.getAxiom();
            changeType = ChangeType.ADD;
            axiom.accept(this);
            return null;
        }

        public Object visit(RemoveAxiomData data) throws RuntimeException {
            OWLAxiom axiom = data.getAxiom();
            changeType = ChangeType.REMOVE;
            axiom.accept(this);
            return null;
        }

        public Object visit(AddOntologyAnnotationData data) throws RuntimeException {
            return null;
        }

        public Object visit(RemoveOntologyAnnotationData data) throws RuntimeException {
            return null;
        }

        public Object visit(SetOntologyIDData data) throws RuntimeException {
            return null;
        }

        public Object visit(AddImportData data) throws RuntimeException {
            return null;
        }

        public Object visit(RemoveImportData data) throws RuntimeException {
            return null;
        }

        @Override
        public void visit(OWLAnnotationAssertionAxiom axiom) {
            OWLAnnotationSubject subject = axiom.getSubject();
            if(!(subject instanceof IRI)) {
                return;
            }
            OWLAnnotationValue value = axiom.getValue();
            if(!(value instanceof OWLLiteral)) {
                return;
            }
            IRI iri = (IRI) subject;
            OWLLiteral literal = (OWLLiteral) value;
            if(axiom.getProperty().getIRI().equals(OWLRDFVocabulary.RDFS_LABEL.getIRI())) {
                if(changeType == ChangeType.ADD) {
                    toMap.put(iri, literal.getLiteral());
                }
                else {
                    fromMap.put(iri, literal.getLiteral());
                }
            }
        }
    }
    
    private enum ChangeType {
        ADD,
        REMOVE
    }
    
}

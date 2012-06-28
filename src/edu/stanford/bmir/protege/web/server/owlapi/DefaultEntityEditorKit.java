package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/05/2012
 */
public class DefaultEntityEditorKit extends OWLAPIEntityEditorKit {


    public DefaultEntityEditorKit(OWLAPIProject project) {
        super(project);
    }


    @Override
    public boolean isBrowserTextEditable(OWLEntity entity) {
        return true;
    }

    @Override
    public String getEntityBrowserText(OWLEntity entity) {
        String fragment = entity.getIRI().getFragment();
        if(fragment != null) {
            return fragment;
        }
        else {
            return entity.getIRI().toString();
        }
    }

    @Override
    public OWLEntityBrowserTextChangeSet setEntityBrowserText(OWLEntity entity, String browserText) {
        return null;
    }

    @Override
    public OWLEntityCreatorFactory getEntityCreatorFactory() {
        return new SimpleOWLEntityCreatorFactory();
    }

    @Override
    public List<OWLEntityBrowserTextChangeSet> getChangedEntities(List<OWLOntologyChangeRecord> ontologyChanges) {
        for(OWLOntologyChangeRecord record : ontologyChanges) {
            OWLOntologyChangeRecordInfo info = record.getInfo();
            
        }
        return Collections.emptyList();
    }

    @Override
    public ShortFormProvider getShortFormProvider() {
        return new SimpleShortFormProvider();
    }

    @Override
    public void dispose() {
    }
    
    
    private class BrowserTextChangeDetector extends OWLAxiomVisitorAdapter implements OWLOntologyChangeRecordInfoVisitor<Object, RuntimeException> {

        private ChangeType changeType;
        
        private Map<OWLEntity, String> fromMap = new HashMap<OWLEntity, String>();
        
        private Map<OWLEntity, String> toMap = new HashMap<OWLEntity, String>();
        
        public Object visit(AddAxiomChangeRecordInfo data) throws RuntimeException {
            changeType = ChangeType.ADD;
            data.getAxiom().accept(this);
            return null;
        }

        public Object visit(RemoveAxiomChangeRecordInfo data) throws RuntimeException {
            changeType = ChangeType.REMOVE;
            return null;
        }

        public Object visit(AddOntologyAnnotationChangeRecordInfo data) throws RuntimeException {
            return null;
        }

        public Object visit(RemoveOntologyAnnotationChangeRecordInfo data) throws RuntimeException {
            return null;
        }

        public Object visit(SetOntologyIDChangeRecordInfo data) throws RuntimeException {
            return null;
        }

        public Object visit(AddImportChangeRecordInfo data) throws RuntimeException {
            return null;
        }

        public Object visit(RemoveImportChangeRecordInfo data) throws RuntimeException {
            return null;
        }

        @Override
        public void visit(OWLDeclarationAxiom axiom) {
            if(changeType == ChangeType.ADD) {
                fromMap.put(axiom.getEntity(), getShortFormProvider().getShortForm(axiom.getEntity()));
            }
            else {
                fromMap.put(axiom.getEntity(), getShortFormProvider().getShortForm(axiom.getEntity()));
            }
        }

        
    }

    private enum ChangeType {
        ADD,
        REMOVE
    }
}

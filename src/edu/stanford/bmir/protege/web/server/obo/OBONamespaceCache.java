package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.client.rpc.data.obo.OBONamespace;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import org.coode.owlapi.obo.parser.OBOVocabulary;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBONamespaceCache {

    private Set<OBONamespace> namespaceCache = new HashSet<OBONamespace>();

    private ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    private Lock READ_LOCK = READ_WRITE_LOCK.readLock();

    private Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    private OWLAPIProject project;


    public static OBONamespaceCache createCache(OWLAPIProject project) {
        return new OBONamespaceCache(project);
    }

    private OBONamespaceCache(OWLAPIProject project) {
        this.project = project;
        rebuildNamespaceCache();
    }

    private void rebuildNamespaceCache() {
        Set<OBONamespace> namespaces = new HashSet<OBONamespace>();
        for(OWLAnnotation anno : project.getRootOntology().getAnnotations()) {
            if(isNamespaceAnnotation(anno)) {
                if(anno.getValue() instanceof OWLLiteral) {
                    OWLLiteral lit = (OWLLiteral) anno.getValue();
                    namespaces.add(new OBONamespace(lit.getLiteral()));
                }
            }
        }
        for(OWLClass cls : project.getRootOntology().getClassesInSignature(true)) {
            for(OWLAnnotationAssertionAxiom ax : cls.getAnnotationAssertionAxioms(project.getRootOntology())) {
                if(isNamespaceAnnotationProperty(ax)) {
                    if(ax.getValue() instanceof OWLLiteral) {
                        OWLLiteral lit = (OWLLiteral) ax.getValue();
                        namespaces.add(new OBONamespace(lit.getLiteral()));
                    }
                }
            }
        }
        try {
            WRITE_LOCK.lock();
            namespaceCache.clear();
            namespaceCache.addAll(namespaces);
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    private boolean isNamespaceAnnotationProperty(OWLAnnotationAssertionAxiom ax) {
        return ax.getProperty().getIRI().equals(OBOVocabulary.NAMESPACE.getIRI());
    }

    private boolean isNamespaceAnnotation(OWLAnnotation annotation) {
        return annotation.getProperty().getIRI().equals(OBOVocabulary.NAMESPACE.getIRI()) || annotation.getProperty().getIRI().equals(OBOVocabulary.DEFAULT_NAMESPACE.getIRI());
    }

    public Set<OBONamespace> getNamespaces() {
        try {
            READ_LOCK.lock();
            return new HashSet<OBONamespace>(namespaceCache);
        }
        finally {
            READ_LOCK.unlock();
        }
    }

}

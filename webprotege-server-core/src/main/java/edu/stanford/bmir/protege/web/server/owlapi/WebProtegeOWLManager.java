package edu.stanford.bmir.protege.web.server.owlapi;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyFactoryImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLOntologyManagerImpl;
import uk.ac.manchester.cs.owl.owlapi.concurrent.ConcurrentOWLOntologyBuilder;
import uk.ac.manchester.cs.owl.owlapi.concurrent.NoOpReadWriteLock;
import uk.ac.manchester.cs.owl.owlapi.concurrent.NonConcurrentOWLOntologyBuilder;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/11/2012
 */
public class WebProtegeOWLManager {

    public static OWLOntologyManager createConcurrentOWLOntologyManager() {
        var dataFactory = new OWLDataFactoryImpl();
        var readWriteLock = new ReentrantReadWriteLock();
        var manager = new OWLOntologyManagerImpl(dataFactory, readWriteLock);
        augmentManager(manager, readWriteLock);
        return manager;
    }

    public static OWLOntologyManager createOWLOntologyManager() {
        var manager = OWLManager.createOWLOntologyManager();
        augmentManager(manager, new NoOpReadWriteLock());
        return manager;
    }

    private static void augmentManager(OWLOntologyManager manager,
                                       ReadWriteLock readWriteLock) {
        var nonConcurrentBuilder = new NonConcurrentOWLOntologyBuilder();
        var concurrentBuilder = new ConcurrentOWLOntologyBuilder(nonConcurrentBuilder, readWriteLock);
        var ontologyFactory = new OWLOntologyFactoryImpl(concurrentBuilder);
        manager.getOntologyFactories().add(ontologyFactory);
        manager.getOntologyParsers().add(new BinaryOWLOntologyDocumentParserFactory());
        manager.getOntologyStorers().add(new BinaryOWLStorerFactory());

    }
}

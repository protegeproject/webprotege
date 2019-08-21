package edu.stanford.bmir.protege.web.server.obo;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.OntologyAnnotationsIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.obo.OBONamespace;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
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
@ProjectSingleton
public class OBONamespaceCache {

    public static final IRI OBO_NAMESPACE_IRI = Obo2OWLConstants.Obo2OWLVocabulary.IRI_OIO_hasOboNamespace.getIRI();

    private ImmutableSet<OBONamespace> namespaceCache = ImmutableSet.of();

    private ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

    private Lock READ_LOCK = READ_WRITE_LOCK.readLock();

    private Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    @Nonnull
    private final OntologyAnnotationsIndex ontologyAnnotationsIndex;

    @Nonnull
    private final DefaultOntologyIdManager defaultOntologyIdManager;

    @Nonnull
    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final OWLDataFactory dataFactory;


    @AutoFactory
    public OBONamespaceCache(@Provided @Nonnull OntologyAnnotationsIndex ontologyAnnotationsIndex,
                              @Provided @Nonnull DefaultOntologyIdManager defaultOntologyIdManager,
                              @Provided @Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                              @Provided @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                              @Provided @Nonnull OWLDataFactory dataFactory) {
        this.ontologyAnnotationsIndex = ontologyAnnotationsIndex;
        this.defaultOntologyIdManager = defaultOntologyIdManager;
        this.axiomsByEntityReferenceIndex = axiomsByEntityReferenceIndex;
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.dataFactory = dataFactory;
    }

    public void rebuildNamespaceCache() {
        var namespacesBuilder = ImmutableSet.<OBONamespace>builder();
        ontologyAnnotationsIndex.getOntologyAnnotations(defaultOntologyIdManager.getDefaultOntologyId())
                                .filter(this::isNamespaceAnnotation)
                                .filter(annotation -> annotation.getValue().isLiteral())
                                .map(annotation -> (OWLLiteral) annotation.getValue())
                                .map(OWLLiteral::getLiteral)
                                .map(OBONamespace::new)
                                .forEach(namespacesBuilder::add);

        var oboNamespaceProperty = dataFactory.getOWLAnnotationProperty(OBO_NAMESPACE_IRI);
        projectOntologiesIndex.getOntologyIds().forEach(ontologyId -> {
            axiomsByEntityReferenceIndex.getReferencingAxioms(oboNamespaceProperty, ontologyId)
                    .filter(OWLAnnotationAssertionAxiom.class::isInstance)
                    .map(OWLAnnotationAssertionAxiom.class::cast)
                    .map(OWLAnnotationAssertionAxiom::getValue)
                    .filter(OWLLiteral.class::isInstance)
                    .map(OWLLiteral.class::cast)
                    .map(OWLLiteral::getLiteral)
                    .map(OBONamespace::new)
                    .forEach(namespacesBuilder::add);
        });

        try {
            WRITE_LOCK.lock();
            namespaceCache = namespacesBuilder.build();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    private boolean isNamespaceAnnotationProperty(OWLAnnotationProperty property) {
        return property.getIRI().equals(OBO_NAMESPACE_IRI);
    }

    private boolean isNamespaceAnnotation(OWLAnnotation annotation) {
        return isNamespaceAnnotationProperty(annotation.getProperty());
    }

    public Set<OBONamespace> getNamespaces() {
        try {
            READ_LOCK.lock();
            return namespaceCache;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

}

package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import org.coode.owlapi.obo.parser.OBOVocabulary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class OBOEntityCreatorFactory extends OWLEntityCreatorFactory {

    public static final String DEFAULT_BASE = "http://purl.obolibrary.org/obo/";


    private final int idLength;

    private String termPrefix;
    

    private final AtomicInteger idGenerator = new AtomicInteger();


    public OBOEntityCreatorFactory(OWLAPIProject project, int idLength) {
        this.idLength = idLength;
        this.termPrefix = getTermPrefix(project);
    }


    private String getTermPrefix(OWLAPIProject project) {
        String ontologyName = getOBOOntologyName(project);
        if (!ontologyName.isEmpty()) {
            return ontologyName.toUpperCase();
        }
        else {
            ProjectId projectId = project.getProjectId();
            String projectName = projectId.getId().trim();
            String uppercaseProjectName = projectName.toUpperCase();
            String escapedUppercaseProjectName = uppercaseProjectName.replaceAll("\\s+", "_");
            // Trim off any digits (version digits) e.g.  VAO2 will become VAO
            return escapedUppercaseProjectName.replaceAll("([^\\d]+)\\s*\\d+", "$1");
        }
    }


    private String getOBOOntologyName(OWLAPIProject project) {
        for (OWLAnnotation annotation : project.getRootOntology().getAnnotations()) {
            if (annotation.getProperty().getIRI().equals(OBOVocabulary.ONTOLOGY.getIRI())) {
                if (annotation.getValue() instanceof OWLLiteral) {
                    return ((OWLLiteral) annotation.getValue()).getLiteral().toUpperCase();
                }
            }
        }
        return "";
    }
    
    
    private String getOBOOntologyNamespace(OWLAPIProject project) {
        for (OWLAnnotation annotation : project.getRootOntology().getAnnotations()) {
            if (annotation.getProperty().getIRI().equals(OBOVocabulary.DEFAULT_NAMESPACE.getIRI())) {
                if (annotation.getValue() instanceof OWLLiteral) {
                    return ((OWLLiteral) annotation.getValue()).getLiteral().toUpperCase();
                }
            }
        }
        return "";
    }

    @Override
    public <E extends OWLEntity> OWLEntityCreator<E> setBrowserText(OWLAPIProject project, UserId userId, E entity, String browserText) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        final OWLDataFactory df = project.getDataFactory();
        OWLLiteral browserTextLiteral = df.getOWLLiteral(browserText, df.getRDFPlainLiteral());
        OWLAxiom freshAx = df.getOWLAnnotationAssertionAxiom(df.getRDFSLabel(), entity.getIRI(), browserTextLiteral);
        for(OWLOntology ont : project.getRootOntology().getImportsClosure()) {
            for(OWLAnnotationAssertionAxiom ax : ont.getAnnotationAssertionAxioms(entity.getIRI())) {
                if(ax.getProperty().isLabel()) {
                    changes.add(new RemoveAxiom(ont, ax));
                    changes.add(new AddAxiom(ont, freshAx));
                }
            }
        }
        if(changes.isEmpty()) {
            changes.add(new AddAxiom(project.getRootOntology(), freshAx));
        }
        return new OWLEntityCreator<E>(entity, changes);

    }

    @Override
    public <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(OWLAPIProject project, UserId userId, String shortName, EntityType<E> entityType) {

        IRI iri = IRI.create(DEFAULT_BASE + getNextId(project));
        OWLDataFactory df = project.getDataFactory();
        E entity = df.getOWLEntity(entityType, iri);

        List<OWLOntologyChange> ontologyChanges = new ArrayList<OWLOntologyChange>();

        OWLOntology rootOntology = project.getRootOntology();

        addDeclarationAxiom(rootOntology, df, entity, ontologyChanges);
        addLabelAnnotationAssertionAxiom(rootOntology, df, entity, shortName, ontologyChanges);

        String namespace = "";
        addNamespaceAnnotationAssertionAxiom(rootOntology, df, entity, namespace, ontologyChanges);

        return new OWLEntityCreator<E>(entity, ontologyChanges);
    }

    private <E extends OWLEntity> void addLabelAnnotationAssertionAxiom(OWLOntology rootOntology, OWLDataFactory df, E entity, String shortName, List<OWLOntologyChange> ontologyChanges) {
        OWLAnnotationProperty rdfsLabel = df.getRDFSLabel();
        OWLLiteral literal = df.getOWLLiteral(shortName, OWL2Datatype.RDF_PLAIN_LITERAL);
        OWLAnnotationAssertionAxiom labelAxiom = df.getOWLAnnotationAssertionAxiom(rdfsLabel, entity.getIRI(), literal);
        ontologyChanges.add(new AddAxiom(rootOntology, labelAxiom));
    }

    private <E extends OWLEntity> void addNamespaceAnnotationAssertionAxiom(OWLOntology rootOntology, OWLDataFactory df, E entity, String namespace, List<OWLOntologyChange> ontologyChanges) {
        if (namespace.isEmpty()) {
            return;
        }
        OWLAnnotationProperty namespaceProperty = df.getOWLAnnotationProperty(OBOVocabulary.NAMESPACE.getIRI());
        OWLLiteral namespaceValue = df.getOWLLiteral(namespace);
        OWLAnnotationAssertionAxiom namespaceAxiom = df.getOWLAnnotationAssertionAxiom(namespaceProperty, entity.getIRI(), namespaceValue);
        ontologyChanges.add(new AddAxiom(rootOntology, namespaceAxiom));
    }

    private <E extends OWLEntity> void addDeclarationAxiom(OWLOntology rootOntology, OWLDataFactory df, E entity, List<OWLOntologyChange> ontologyChanges) {
        OWLDeclarationAxiom declarationAxiom = df.getOWLDeclarationAxiom(entity);
        ontologyChanges.add(new AddAxiom(rootOntology, declarationAxiom));
    }

    /**
     * Gets the next id for an obo term.
     * @param project The project for which the next id should be retrieved
     * @return
     */
    private String getNextId(OWLAPIProject project) {
        OWLOntology rootOntology = project.getRootOntology();
        if (rootOntology.isAnonymous()) {
            throw new RuntimeException("Cannot deal with anonymous ontologies");
        }


        StringBuilder sb = new StringBuilder(termPrefix);
        sb.append("_");
        for (int i = 0; i < idLength; i++) {
            sb.append("0");
        }
        NumberFormat numberFormat = new DecimalFormat(sb.toString());
        // I think this is the only bit of synchronization that we need.  Synchronize on the project object
        synchronized (project) {
            while (true) {
                String shortName = numberFormat.format(idGenerator.getAndIncrement());
                IRI iri = IRI.create(DEFAULT_BASE + shortName);
                if (!rootOntology.containsEntityInSignature(iri, true)) {
                    return shortName;
                }
//                currentId++;
            }
        }
    }


}

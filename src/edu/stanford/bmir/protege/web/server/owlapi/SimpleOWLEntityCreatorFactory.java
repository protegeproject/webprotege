package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.server.IdUtil;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/04/2012
 */
public class SimpleOWLEntityCreatorFactory extends OWLEntityCreatorFactory {

    
    
    @Override
    public <E extends OWLEntity> OWLEntityCreator<E> getEntityCreator(OWLAPIProject project, UserId userId, String shortName, EntityType<E> entityType) {
        OWLOntology rootOntology = project.getRootOntology();
        OWLOntologyID id = rootOntology.getOntologyID();
        String base = getDefaultIRIBase(id, entityType);
        String fragment = createFragment(base, rootOntology);
        IRI entityIRI = IRI.create(base + fragment);
        OWLDataFactory dataFactory = project.getDataFactory();
        E entity = dataFactory.getOWLEntity(entityType, entityIRI);
        OWLDeclarationAxiom declarationAxiom = dataFactory.getOWLDeclarationAxiom(entity);
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.add(new AddAxiom(rootOntology, declarationAxiom));

        OWLLiteral labellingLiteral = dataFactory.getOWLLiteral(shortName, project.getDefaultLanguage());
        OWLAnnotationAssertionAxiom labellingAssertion = dataFactory.getOWLAnnotationAssertionAxiom(dataFactory.getRDFSLabel(), entityIRI, labellingLiteral);
        changes.add(new AddAxiom(rootOntology, labellingAssertion));
        return new OWLEntityCreator<E>(entity, changes);
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

    private String createFragment(String base, OWLOntology ontology) {
        while (true) {
            String frag = IdUtil.getBase62UUID();
            StringBuilder sb = new StringBuilder();
            sb.append(base);
            sb.append(frag);
            IRI iri = IRI.create(sb.toString());
            if(!ontology.containsEntityInSignature(iri)) {
                return frag;
            }
        }
    }
}

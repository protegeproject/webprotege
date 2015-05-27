package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.shared.HasGetChangeSubjects;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.AxiomSubjectProvider;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class OntologyChangeSubjectProvider implements HasGetChangeSubjects {

    private ChangeSubjectProvider changeSubjectProvider;

    @Inject
    public OntologyChangeSubjectProvider(HasGetEntitiesWithIRI entitiesWithIRI) {
        this.changeSubjectProvider = new ChangeSubjectProvider(new AxiomEntitySubjectProvider(entitiesWithIRI));
    }

    @Override
    public Set<OWLEntity> getChangeSubjects(OWLOntologyChange change) {
        return change.accept(changeSubjectProvider);
    }



    private static class ChangeSubjectProvider implements OWLOntologyChangeVisitorEx<Set<OWLEntity>> {

        private AxiomEntitySubjectProvider subjectProvider;

        private ChangeSubjectProvider(AxiomEntitySubjectProvider subjectProvider) {
            this.subjectProvider = subjectProvider;
        }

        @Override
        public Set<OWLEntity> visit(AddAxiom addAxiom) {
            return subjectProvider.getSubject(addAxiom.getAxiom());
        }

        @Override
        public Set<OWLEntity> visit(RemoveAxiom removeAxiom) {
            return subjectProvider.getSubject(removeAxiom.getAxiom());
        }

        @Override
        public Set<OWLEntity> visit(SetOntologyID setOntologyID) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(AddImport addImport) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(RemoveImport removeImport) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(AddOntologyAnnotation addOntologyAnnotation) {
            return Collections.emptySet();
        }

        @Override
        public Set<OWLEntity> visit(RemoveOntologyAnnotation removeOntologyAnnotation) {
            return Collections.emptySet();
        }
    }


    private static class AxiomEntitySubjectProvider {

        private HasGetEntitiesWithIRI hasGetEntitiesWithIRI;

        private AxiomEntitySubjectProvider(HasGetEntitiesWithIRI hasGetEntitiesWithIRI) {
            this.hasGetEntitiesWithIRI = hasGetEntitiesWithIRI;
        }

        public Set<OWLEntity> getSubject(OWLAxiom axiom) {
            OWLObject subject = new AxiomSubjectProvider().getSubject(axiom);
            if(subject instanceof OWLEntity) {
                return Collections.singleton((OWLEntity) subject);
            }
            else if(subject instanceof IRI) {
                return hasGetEntitiesWithIRI.getEntitiesWithIRI((IRI) subject);
            }
            else {
                return Collections.emptySet();
            }
        }
    }

}

package edu.stanford.bmir.protege.web.server.frame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public class OntologySubsetObjectPropertyFrameTranslator {
//
//    private ProjectId projectId;
//
//    public OntologySubsetObjectPropertyFrameTranslator(ProjectId projectId) {
//        this.projectId = projectId;
//    }
//
//    public ObjectPropertyFrame getFrame(OWLObjectProperty subject) {
//        NamedIndividualFrameTranslator translator = new NamedIndividualFrameTranslator();
//        OWLOntology rootOntology = OWLAPIProjectManager.getProjectManager().getProject(projectId).getRootOntology();
//        return translator.translateToNamedIndividualFrame(subject, getCandidateAxioms(subject), rootOntology);
//    }
//
//
//    public void applyChanges(ObjectPropertyFrame from, ObjectPropertyFrame to, UserId userId) {
//        ObjectPropertyFrame serverClassFrame = getFrame(to.getSubject());
//        if(serverClassFrame.equals(to)) {
//            // Nothing to do
//            return;
//        }
//
//
//        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
//        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
//
//        Set<OWLOntology> importsClosure = project.getRootOntology().getImportsClosure();
//
//
//        Set<OWLAxiom> ontologyCandidateAxioms = getCandidateAxioms(from.getSubject());
//
//
//        // Changes to apply changes
//
//        NamedIndividualFrameTranslator translator = new NamedIndividualFrameTranslator();
//
//        Set<OWLAxiom> toAxioms = translator.translateToAxioms(to.getSubject(), to);
//
//        for(OWLAxiom fromAxiom : ontologyCandidateAxioms) {
//            if(!toAxioms.contains(fromAxiom)) {
//                for (OWLOntology ont : importsClosure) {
//                    changes.add(new RemoveAxiom(ont, fromAxiom));
//                }
//            }
//        }
//
//        for(OWLAxiom toAxiom : toAxioms) {
//            if(!ontologyCandidateAxioms.contains(toAxiom)) {
//                for (OWLOntology ont : importsClosure) {
//                    changes.add(new AddAxiom(ont, toAxiom));
//                }
//            }
//        }
//
//        // Collapse?!?!?!
//
//
//        project.applyChanges(userId, changes, "Edited property values");
//    }
//
//
//
//    private Set<OWLAxiom> getCandidateAxioms(OWLObjectProperty subject) {
//        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
//        Set<OWLAxiom> allAxioms = new HashSet<OWLAxiom>();
//        OWLOntology ont = project.getRootOntology();
//        allAxioms.addAll(ont.getAnnotationAssertionAxioms(subject.getIRI()));
//        allAxioms.addAll(ont.getObjectPropertyDomainAxioms(subject));
//        allAxioms.addAll(ont.getObjectPropertyRangeAxioms(subject));
//        return allAxioms;
//
//    }

}

package edu.stanford.bmir.protege.web.server.frame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/12/2012
 */
public class OntologySubsetNamedIndividualFrameTranslator {
//
//    private ProjectId projectId;
//
//    public OntologySubsetNamedIndividualFrameTranslator(ProjectId projectId) {
//        this.projectId = projectId;
//    }
//
//    public NamedIndividualFrame getFrame(OWLNamedIndividual subject) {
//        NamedIndividualFrameTranslator translator = new NamedIndividualFrameTranslator();
//        AxiomRewriting rewriting = getRewrittenCandidateAxioms(subject);
//        OWLOntology rootOntology = OWLAPIProjectManager.getProjectManager().getProject(projectId).getRootOntology();
//        return translator.getFrame(subject, rewriting.getRewrittenAxioms(), rootOntology);
//    }
//
//    private AxiomRewriting getRewrittenCandidateAxioms(OWLNamedIndividual subject) {
//        Set<OWLAxiom> candidateAxioms = getCandidateAxioms(subject);
//        AxiomRewriter rewriter = new SubClassAxiomSplitter();
//        AxiomRewriting.Builder rewritingBuilder = new AxiomRewriting.Builder();
//
//        for(OWLAxiom candidateAxiom : candidateAxioms) {
//            Set<OWLAxiom> rewrittenAxioms = rewriter.rewriteAxiom(candidateAxiom);
//            rewritingBuilder.addRewriting(candidateAxiom, rewrittenAxioms);
//        }
//        return rewritingBuilder.build();
//    }
//
//    public void applyChanges(NamedIndividualFrame from, NamedIndividualFrame to, UserId userId) {
//        NamedIndividualFrame serverClassFrame = getFrame(to.getSubject());
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
//        Set<OWLAxiom> rewrittenCandidateAxioms = getRewrittenCandidateAxioms(from.getSubject()).getRewrittenAxioms();
//
//        // Changes to rewrite axioms
//
//        for(OWLAxiom ontAx : ontologyCandidateAxioms) {
//            if(!rewrittenCandidateAxioms.contains(ontAx)) {
//                for(OWLOntology ont : importsClosure) {
//                    changes.add(new RemoveAxiom(ont, ontAx));
//                }
//            }
//        }
//
//        for(OWLAxiom rewrittenAx : rewrittenCandidateAxioms) {
//            if(!ontologyCandidateAxioms.contains(rewrittenAx)) {
//                for(OWLOntology ont : importsClosure) {
//                    changes.add(new AddAxiom(ont, rewrittenAx));
//                }
//            }
//        }
//
//
//        // Changes to apply changes
//
//        NamedIndividualFrameTranslator translator = new NamedIndividualFrameTranslator();
//
//        Set<OWLAxiom> toAxioms = translator.translateToAxioms(to.getSubject(), to);
//
//        for(OWLAxiom fromAxiom : rewrittenCandidateAxioms) {
//            if(!toAxioms.contains(fromAxiom)) {
//                for (OWLOntology ont : importsClosure) {
//                    changes.add(new RemoveAxiom(ont, fromAxiom));
//                }
//            }
//        }
//
//        for(OWLAxiom toAxiom : toAxioms) {
//            if(!rewrittenCandidateAxioms.contains(toAxiom)) {
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
//    private Set<OWLAxiom> getCandidateAxioms(OWLNamedIndividual subject) {
//        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
//        Set<OWLAxiom> allAxioms = new HashSet<OWLAxiom>();
//        OWLOntology ont = project.getRootOntology();
//        allAxioms.addAll(ont.getClassAssertionAxioms(subject));
//        allAxioms.addAll(ont.getObjectPropertyAssertionAxioms(subject));
//        allAxioms.addAll(ont.getDataPropertyAssertionAxioms(subject));
//        allAxioms.addAll(ont.getAnnotationAssertionAxioms(subject.getIRI()));
//        return allAxioms;
//
//    }
}

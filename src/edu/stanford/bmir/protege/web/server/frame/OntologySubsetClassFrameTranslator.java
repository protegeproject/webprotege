package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/12/2012
 */
public class OntologySubsetClassFrameTranslator {


    private ProjectId projectId;

    public OntologySubsetClassFrameTranslator(ProjectId projectId) {
        this.projectId = projectId;
    }

    public ClassFrame getClassFrame(OWLClass subject) {
        ClassFrameTranslator translator = new ClassFrameTranslator();
        final OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
        OWLOntology rootOntology = project.getRootOntology();
        return translator.getFrame(subject, rootOntology, project);
    }

//    private AxiomRewriting getRewrittenCandidateAxioms(OWLClass subject) {
//        Set<OWLAxiom> candidateAxioms = getCandidateAxioms(subject);
//        AxiomRewriter rewriter = new SubClassAxiomSplitter();
//        AxiomRewriting.Builder rewritingBuilder = new AxiomRewriting.Builder();
//
//        for (OWLAxiom candidateAxiom : candidateAxioms) {
//            Set<OWLAxiom> rewrittenAxioms = rewriter.rewriteAxiom(candidateAxiom);
//            rewritingBuilder.addRewriting(candidateAxiom, rewrittenAxioms);
//        }
//        return rewritingBuilder.build();
//    }

    public void applyChanges(ClassFrame from, ClassFrame to, UserId userId) {
        ClassFrame serverClassFrame = getClassFrame(to.getSubject());
        if (serverClassFrame.equals(to)) {
            // Nothing to do
            return;
        }

        ClassFrameTranslator translator = new ClassFrameTranslator();

        // Get the axioms that were consumed in the translation
        Set<OWLAxiom> consumedAxioms = translator.getAxioms(serverClassFrame);


        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        Set<OWLOntology> importsClosure = project.getRootOntology().getImportsClosure();


        Set<OWLAxiom> ontologyCandidateAxioms = getCandidateAxioms(from.getSubject());
//        Set<OWLAxiom> rewrittenCandidateAxioms = getRewrittenCandidateAxioms(from.getSubject()).getRewrittenAxioms();



        // Axioms which aren't used in the translation
        Set<OWLAxiom> nonConsumedAxioms = new HashSet<OWLAxiom>(ontologyCandidateAxioms);
        nonConsumedAxioms.removeAll(consumedAxioms);

        // Changes to rewrite axioms

        for (OWLAxiom ontAx : ontologyCandidateAxioms) {
            if (!ontologyCandidateAxioms.contains(ontAx)) {
                for (OWLOntology ont : importsClosure) {
                    changes.add(new RemoveAxiom(ont, ontAx));
                }
            }
        }

        for (OWLAxiom rewrittenAx : ontologyCandidateAxioms) {
            if (!ontologyCandidateAxioms.contains(rewrittenAx)) {
                for (OWLOntology ont : importsClosure) {
                    changes.add(new AddAxiom(ont, rewrittenAx));
                }
            }
        }

        // Changes to apply changes


        Set<OWLAxiom> toAxioms = translator.getAxioms(to);

        for (OWLAxiom fromAxiom : ontologyCandidateAxioms) {
            if (!toAxioms.contains(fromAxiom) && !nonConsumedAxioms.contains(fromAxiom)) {
                for (OWLOntology ont : importsClosure) {
                    changes.add(new RemoveAxiom(ont, fromAxiom));
                }
            }
        }

        for (OWLAxiom toAxiom : toAxioms) {
            if (!ontologyCandidateAxioms.contains(toAxiom)) {
                for (OWLOntology ont : importsClosure) {
                    changes.add(new AddAxiom(ont, toAxiom));
                }
            }
        }

        // Collapse?!?!?!


        project.applyChanges(userId, changes, "Edited property values");
    }


    private Set<OWLAxiom> getCandidateAxioms(OWLClass subject) {
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
        Set<OWLAxiom> allAxioms = new HashSet<OWLAxiom>();
        Set<OWLSubClassOfAxiom> axiomSet = project.getRootOntology().getSubClassAxiomsForSubClass(subject);
        allAxioms.addAll(axiomSet);
        allAxioms.addAll(project.getRootOntology().getAnnotationAssertionAxioms(subject.getIRI()));
        return allAxioms;

    }

}

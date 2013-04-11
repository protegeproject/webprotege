package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.GeneratedOntologyChanges;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.frame.Frame;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public final class FrameChangeGenerator<F extends Frame<S>, S extends OWLEntity> implements ChangeListGenerator<S> {


    private final F from;

    private final F to;

    private final FrameTranslator<F, S> translator;

    public FrameChangeGenerator(F from, F to, FrameTranslator<F, S> translator) {
        this.from = from;
        this.to = to;
        this.translator = translator;
    }

    public List<OWLOntologyChange> createChanges(final OWLOntology rootOntology, OWLAPIProject project) {
        // TODO: Consider axiom annotations!

        // TODO: Three way merge incase the frame has been modified "externally" and is different from the original
        // TODO: from frame

        F serverFrame = translator.getFrame(to.getSubject(), rootOntology, project);
        if (serverFrame.equals(to)) {
            // Nothing to do
            return Collections.emptyList();
        }


        // Get the axioms that were consumed in the translation
        Set<OWLAxiom> fromAxioms = translator.getAxioms(from);

        Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();

        Set<OWLAxiom> toAxioms = translator.getAxioms(to);

        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLAxiom fromAxiom : fromAxioms) {
            if (!toAxioms.contains(fromAxiom)) {
                for (OWLOntology ont : importsClosure) {
                    changes.add(new RemoveAxiom(ont, fromAxiom));
                }
            }
        }
        for (OWLAxiom toAxiom : toAxioms) {
            if (!fromAxioms.contains(toAxiom)) {
                for (OWLOntology ont : importsClosure) {
                    changes.add(new AddAxiom(ont, toAxiom));
                }
            }
        }
        // TODO:  Remove redundant changes
        return changes;
    }

    @Override
    public GeneratedOntologyChanges<S> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
        GeneratedOntologyChanges.Builder<S> b = new GeneratedOntologyChanges.Builder<S>();
        b.addAll(createChanges(project.getRootOntology(), project));
        return b.build(to.getSubject());
    }

    @Override
    public S getRenamedResult(S result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}

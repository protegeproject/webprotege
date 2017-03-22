package edu.stanford.bmir.protege.web.server.frame;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.server.change.ChangeGenerationContext;
import edu.stanford.bmir.protege.web.server.change.ChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeList;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.Frame;
import org.semanticweb.owlapi.model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 */
public final class FrameChangeGenerator<F extends Frame<S>, S extends OWLEntityData> implements ChangeListGenerator<S> {


    private final F from;

    private final F to;

    private final FrameTranslator<F, S> translator;

    public FrameChangeGenerator(F from, F to, FrameTranslator<F, S> translator) {
        this.from = from;
        this.to = to;
        this.translator = translator;
    }

    public List<OWLOntologyChange> createChanges(final OWLOntology rootOntology, Project project) {
        // TODO: Consider axiom annotations!

        // TODO: Three way merge incase the frame has been modified "externally" and is different from the original
        // TODO: from frame


        // This looks like it is more complicated than necessary, however we need to consider the fact that
        // a frame may be generated from multiple axioms (note the minimal and maximal translation)

        F serverFrame = translator.getFrame(to.getSubject(), rootOntology, project);
        if (serverFrame.equals(to)) {
            // Nothing to do
            return Collections.emptyList();
        }


        // Get the axioms that were consumed in the translation
        Set<OWLAxiom> fromAxioms = translator.getAxioms(from, Mode.MAXIMAL);

        List<OWLOntology> importsClosure = rootOntology.getOWLOntologyManager().getSortedImportsClosure(rootOntology);

        Set<OWLAxiom> toAxioms = translator.getAxioms(to, Mode.MINIMAL);


        Multimap<OWLAxiom, OWLOntology> axiom2OntologyMap = LinkedHashMultimap.create();

        // Generate a map of existing axioms so we can ensure they stay in the correct place
        for(OWLOntology ontology : importsClosure) {
            for(OWLAxiom fromAxiom : fromAxioms) {
                if(isContainedInOntology(fromAxiom, ontology)) {
                    axiom2OntologyMap.put(fromAxiom, ontology);
                }
            }
        }


        Set<OWLOntology> mutatedOntologies = Sets.newLinkedHashSet();
        List<OWLOntologyChange> changes = Lists.newArrayList();
        for (OWLAxiom fromAxiom : fromAxioms) {
            for (OWLOntology ont : importsClosure) {
                if (isContainedInOntology(fromAxiom, ont) && !toAxioms.contains(fromAxiom)) {
                    mutatedOntologies.add(ont);
                }
                // We need to add this here in case an axiom containing fresh entities has been added and then
                // removed (caused by a re-edit).  The fresh entities will get "grounded" and then the axiom added
                // hence, ontology.contains() won't work.
                changes.add(new RemoveAxiom(ont, fromAxiom));
            }
        }


        for(OWLAxiom toAxiom : toAxioms) {
            Collection<OWLOntology> existingLocations = axiom2OntologyMap.get(toAxiom);
            if(existingLocations.isEmpty()) {
                // Fresh axiom to be placed somewhere
                if(mutatedOntologies.size() == 1) {
                    // Assume edit i.e. replacement of axiom in the same ontology.
                    changes.add(new AddAxiom(mutatedOntologies.iterator().next(), toAxiom));
                }
                else {
                    // Multiple ontologies were affected.  We now need to place the fresh axiom in the appropriate
                    // ontology
                    OWLOntology freshAxiomOntology = getFreshAxiomOntology(fromAxioms, importsClosure, rootOntology);
                    changes.add(new AddAxiom(freshAxiomOntology, toAxiom));
                }
            }
            else {
                // Ensure it is still in there
                for(OWLOntology ont : existingLocations) {
                    changes.add(new AddAxiom(ont, toAxiom));
                }
            }
        }
        return changes;
    }

    private boolean isContainedInOntology(OWLAxiom axiom, OWLOntology ont) {
        // An optimisation.  Try containsAxiom first, then try containsAxiomIgnoreAnnotations.
        // Do this because containsAxiomIgnoreAnnotations requires
        // an iteration over all axioms of the same type in the default implementation.
        return ont.containsAxiom(axiom) || ont.containsAxiomIgnoreAnnotations(axiom);
    }

    private OWLOntology getFreshAxiomOntology(Set<OWLAxiom> fromAxioms, List<OWLOntology> importsClosure, OWLOntology rootOntology) {
        for(OWLOntology ont : importsClosure) {
            for(OWLAxiom existingFrameAxiom : fromAxioms) {
                if(isContainedInOntology(existingFrameAxiom, ont)) {
                    return ont;
                }
            }
        }
        return rootOntology;
    }

    @Override
    public OntologyChangeList<S> generateChanges(Project project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<S> b = new OntologyChangeList.Builder<S>();
        b.addAll(createChanges(project.getRootOntology(), project));
        return b.build(to.getSubject());
    }

    @Override
    public S getRenamedResult(S result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}

package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.server.project.Project;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.Collections;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesChangeGenerator extends AbstractCreateEntitiesChangeListGenerator<OWLClass, OWLClass> {

    public CreateClassesChangeGenerator(Set<String> browserTexts, Optional<OWLClass> parent) {
        super(EntityType.CLASS, browserTexts, parent);
    }

    @Override
    protected Set<OWLAxiom> createParentPlacementAxioms(OWLClass freshEntity, Project project, ChangeGenerationContext context, Optional<OWLClass> parent) {
        if (parent.isPresent()) {
            OWLAxiom ax = project.getDataFactory().getOWLSubClassOfAxiom(freshEntity, parent.get());
            return Collections.singleton(ax);
        }
        else {
            return Collections.emptySet();
        }
    }

    //    private OWLClass superClass;
//
//    private Set<String> freshClassesBrowserText;
//
//    public CreateClassesChangeGenerator(OWLClass superClass, Set<String> freshClassesBrowserText) {
//        this.superClass = superClass;
//        this.freshClassesBrowserText = freshClassesBrowserText;
//    }
//
//    public Set<String> getFreshClassesBrowserText() {
//        return new HashSet<String>(freshClassesBrowserText);
//    }
//
//    public OWLClass getSuperClass() {
//        return superClass;
//    }
//
//
//    @Override
//    public GeneratedOntologyChanges<Set<OWLClass>> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
//        Set<OWLClass> generatedClasses = new HashSet<OWLClass>();
//        GeneratedOntologyChanges.Builder<Set<OWLClass>> builder = new GeneratedOntologyChanges.Builder<Set<OWLClass>>();
//        for(String browserText : freshClassesBrowserText) {
//            CreateClassChangeGenerator gen = new CreateClassChangeGenerator(browserText, superClass);
//            GeneratedOntologyChanges<OWLClass> changes = gen.generateChanges(project, context);
//            generatedClasses.addAll(changes.getResult().asSet());
//            builder.addAll(changes.getChanges());
//        }
//        return builder.build(generatedClasses);
//    }
//
//    @Override
//    public Set<OWLClass> getRenamedResult(Set<OWLClass> result, RenameMap renameMap) {
//        Set<OWLClass> renamedResult = new HashSet<OWLClass>();
//        for(OWLClass cls : result) {
//            renamedResult.add(renameMap.getRenamedEntity(cls));
//        }
//        return renamedResult;
//    }
}

package edu.stanford.bmir.protege.web.server.index;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-11-09
 */
public interface ClassHierarchyChildrenAxiomsIndex extends Index {

    @Nonnull
    Stream<OWLClassAxiom> getChildrenAxioms(@Nonnull OWLClass cls);

    boolean isLeaf(@Nonnull OWLClass cls);
}

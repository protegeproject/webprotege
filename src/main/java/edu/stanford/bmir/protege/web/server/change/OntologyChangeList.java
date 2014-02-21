package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasResult;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 * <p>
 *     A list of ontology changes plus an optional result of applying the changes.
 * </p>
 */
public class OntologyChangeList<R> implements HasResult<Optional<R>> {

    private Optional<R> result;

    private List<OWLOntologyChange> changes;

    /**
     * Constructs a {@link OntologyChangeList} object which bundles together a list of ontology changes with
     * a main result.
     * @param changes The list of ontology changes.  Not {@code null}.
     * @param result The main result. Not {@code null}.
     */
    private OntologyChangeList(List<OWLOntologyChange> changes, Optional<R> result) {
        this.result = result;
        this.changes = new ArrayList<OWLOntologyChange>(changes);
    }

    /**
     * Gets the list of changes.
     * @return An immutable list of changes.
     */
    public List<OWLOntologyChange> getChanges() {
        return Collections.unmodifiableList(changes);
    }

    /**
     * Gets the main result of generating the changes.
     * @return The result.  Not {@code null}.
     */
    @Override
    public Optional<R> getResult() {
        return result;
    }

    public static <R> Builder<R> builder() {
        return new Builder<R>();
    }

    /**
     * A builder for building a {@link OntologyChangeList} object.
     * @param <R> The type of result.
     */
    public static class Builder<R> {

        private List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        public Builder() {
        }

        public void add(OWLOntologyChange change) {
            changes.add(change);
        }

        public boolean isEmpty() {
            return changes.isEmpty();
        }

        public void addAxiom(OWLOntology ontology, OWLAxiom axiom) {
            changes.add(new AddAxiom(ontology, axiom));
        }

        public void addAxioms(OWLOntology ontology, Set<? extends OWLAxiom> axioms) {
            for(OWLAxiom axiom : axioms) {
                addAxiom(ontology, axiom);
            }
        }

        public void removeAxiom(OWLOntology ontology, OWLAxiom axiom) {
            changes.add(new RemoveAxiom(ontology, axiom));
        }

        public void removeAxioms(OWLOntology ontology, Set<? extends OWLAxiom> axioms) {
            for(OWLAxiom axiom : axioms) {
                removeAxiom(ontology, axiom);
            }
        }

        public void addAll(List<OWLOntologyChange> changes) {
            this.changes.addAll(changes);
        }

        public OntologyChangeList<R> build(R subject) {
            return build(Optional.of(subject));
        }

        public OntologyChangeList<R> build() {
            return build(Optional.<R>absent());
        }


        private OntologyChangeList<R> build(Optional<R> subject) {
            return new OntologyChangeList<R>(changes, subject);
        }


    }
}

package edu.stanford.bmir.protege.web.server.change;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasResult;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class GeneratedOntologyChanges<R> implements HasResult<Optional<R>> {

    private Optional<R> result;

    private List<OWLOntologyChange> changes;

    /**
     * Constructs a {@link GeneratedOntologyChanges} object which bundles together a list of ontology changes with
     * a main result.
     * @param changes The list of ontology changes.  Not {@code null}.
     * @param result The main result. Not {@code null}.
     */
    private GeneratedOntologyChanges(List<OWLOntologyChange> changes, Optional<R> result) {
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

    /**
     * A builder for building a {@link GeneratedOntologyChanges} object.
     * @param <R> The type of result.
     */
    public static class Builder<R> {

        private List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        public Builder() {
        }

        public void add(OWLOntologyChange change) {
            changes.add(change);
        }


        public void addAll(List<OWLOntologyChange> changes) {
            this.changes.addAll(changes);
        }

        public GeneratedOntologyChanges<R> build(R subject) {
            return build(Optional.of(subject));
        }

        public GeneratedOntologyChanges<R> build() {
            return build(Optional.<R>absent());
        }


        private GeneratedOntologyChanges<R> build(Optional<R> subject) {
            return new GeneratedOntologyChanges<R>(changes, subject);
        }


    }
}

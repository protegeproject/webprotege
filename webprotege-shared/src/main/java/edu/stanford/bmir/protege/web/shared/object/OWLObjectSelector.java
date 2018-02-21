package edu.stanford.bmir.protege.web.shared.object;

import org.semanticweb.owlapi.model.OWLObject;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/01/15
 */
public interface OWLObjectSelector<O extends OWLObject> {

    /**
     * Selects an {@link OWLObject} from the iterable of objects.
     * @param objects The iterable of objects.  Not {@code null}.  Note, that the iterable may not contain any
     *                objects.
     * @return A (possibly absent) choice from the objects.  Not {@code null}.
     */
    Optional<O> selectOne(Iterable<O> objects);
}

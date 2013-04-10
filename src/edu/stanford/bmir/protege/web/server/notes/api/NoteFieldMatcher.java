package edu.stanford.bmir.protege.web.server.notes.api;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.notes.NoteField;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
public interface NoteFieldMatcher<T extends Serializable> {

    boolean matches(NoteField<T> field, Optional<T> value);
}

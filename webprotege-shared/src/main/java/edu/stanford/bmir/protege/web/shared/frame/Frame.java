package edu.stanford.bmir.protege.web.shared.frame;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.HasSignature;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 14/01/2013
 * <p>
 *     A high level interface for frame objects.  All frames have some kind of subject.
 * </p>
 */
public interface Frame<S> extends HasSignature, IsSerializable {

    /**
     * Gets the subject of this frame.
     * @return The subject. Not {@code null}.
     */
    S getSubject();
}

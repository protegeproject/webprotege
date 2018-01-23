package edu.stanford.bmir.protege.web.client.diff;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.diff.DiffElement;

import java.io.Serializable;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
public interface DiffView extends IsWidget {

    <S extends Serializable, E extends Serializable> void setDiff(List<DiffElement<S, E>> element,
                                                                  DiffLineElementRenderer<E> renderer,
                                                                  DiffSourceDocumentRenderer<S> sourceDocumentRenderer);
}

package edu.stanford.bmir.protege.web.client.primitive;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/01/2014
 * <p>
 *     A view that can be displayed to suggest types for fresh entities.
 * </p>
 */
public interface PrimitiveDataEditorFreshEntityView extends IsWidget, HasSelectionHandlers<EntityType<?>> {

    void setExpectedTypes(SafeHtml errorMessage, Set<EntityType<?>> suggestedTypes);

}

package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.gwt.user.client.ui.HasVisibility;
import org.semanticweb.owlapi.model.EntityType;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 20/03/2014
 */
public interface ManchesterSyntaxFrameEditorErrorMessageView extends HasVisibility {

    void setErrorMessage(String message);

    void setCurrentToken(String currentToken);

    void setExpectedEntityTypes(List<EntityType<?>> expectedEntityTypes);


}

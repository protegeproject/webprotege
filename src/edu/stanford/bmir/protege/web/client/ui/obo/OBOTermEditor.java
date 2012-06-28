package edu.stanford.bmir.protege.web.client.ui.obo;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.Entity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/05/2012
 */
public interface OBOTermEditor {

    int getEditorCount();
    
    String getLabel(int index);

    Widget getEditorWidget(int index);
    
    boolean hasXRefs();

    XRefListEditor getXRefListEditor();

    boolean isDirty();
}

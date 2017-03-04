package edu.stanford.bmir.protege.web.client.frame;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.common.HasEditable;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrame;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
public interface ClassFrameEditorPresenter extends ValueEditor<LabelledFrame<ClassFrame>>, HasEnabled, HasEditable, HasWidgets {

    public Widget getWidget();

    boolean isDirty();


}

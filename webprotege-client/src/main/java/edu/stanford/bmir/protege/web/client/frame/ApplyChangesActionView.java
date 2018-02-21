package edu.stanford.bmir.protege.web.client.frame;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 25/03/2014
 */
public interface ApplyChangesActionView extends IsWidget, HasVisibility {

    void setHandler(ApplyChangesActionHandler handler);

}

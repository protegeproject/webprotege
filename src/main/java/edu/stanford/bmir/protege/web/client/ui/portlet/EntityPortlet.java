package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import java.util.Collection;

public interface EntityPortlet extends IsWidget, HasDispose {

    void setWidth(int width);

    void setHeight(int height);

    void setAutoHeight(boolean b);

    void setAutoWidth(boolean b);
}

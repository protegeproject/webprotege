package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.PortletId;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public interface PortletChooserView extends IsWidget {

    Optional<PortletId> getSelectedPortletId();

    void setAvailablePortlets(Collection<PortletDescriptor> portlets);

}

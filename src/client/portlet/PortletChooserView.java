package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.PortletId;

import java.util.Collection;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public interface PortletChooserView extends IsWidget {

    Optional<PortletId> getSelectedPortletId();

    void setAvailablePortlets(Collection<PortletDescriptor> portlets);

}

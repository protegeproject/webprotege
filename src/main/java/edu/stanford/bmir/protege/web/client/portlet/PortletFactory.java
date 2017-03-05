package edu.stanford.bmir.protege.web.client.portlet;

import edu.stanford.bmir.protege.web.shared.PortletId;

import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public interface PortletFactory {

    List<PortletDescriptor> getAvailablePortletDescriptors();

    Optional<WebProtegePortlet> createPortlet(PortletId portletId);
}

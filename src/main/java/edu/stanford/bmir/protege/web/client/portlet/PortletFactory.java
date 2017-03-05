package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.PortletId;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public interface PortletFactory {

    List<PortletDescriptor> getAvailablePortletDescriptors();

    Optional<WebProtegePortletPresenter> createPortlet(PortletId portletId);
}

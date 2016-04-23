package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.PortletId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18/04/16
 */
public interface PortletFactory {

    Optional<WebProtegePortlet> createPortlet(PortletId portletId);
}

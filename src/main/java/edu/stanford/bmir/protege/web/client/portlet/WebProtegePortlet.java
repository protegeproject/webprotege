package edu.stanford.bmir.protege.web.client.portlet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 5 Mar 2017
 */
public class WebProtegePortlet {

    private final WebProtegePortletPresenter presenter;

    private final PortletDescriptor portletDescriptor;

    public WebProtegePortlet(WebProtegePortletPresenter presenter,
                             PortletDescriptor portletDescriptor) {
        this.presenter = presenter;
        this.portletDescriptor = portletDescriptor;
    }

    public WebProtegePortletPresenter getPresenter() {
        return presenter;
    }

    public PortletDescriptor getPortletDescriptor() {
        return portletDescriptor;
    }
}

package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@Portlet(id = "portlets.viz", title = "Viz", tooltip = "Provides a visualisation")
public class VizPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final FlowPanel panel = new FlowPanel();

    @Inject
    public VizPortletPresenter(@Nonnull SelectionModel selectionModel, @Nonnull ProjectId projectId, @Nonnull DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(panel);
        Viz viz = new Viz();
        viz.render("digraph {a -> b;}", ren -> {
            panel.getElement().setInnerHTML(ren);
        });

    }
}

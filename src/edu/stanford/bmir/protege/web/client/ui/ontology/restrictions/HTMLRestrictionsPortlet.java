package edu.stanford.bmir.protege.web.client.ui.ontology.restrictions;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

public class HTMLRestrictionsPortlet extends AbstractEntityPortlet {

	private Panel panel;

	public HTMLRestrictionsPortlet(Project project) {
		super(project);
	}

	@Override
	public void initialize() {
		setTitle("Axioms");
		setLayout(new AnchorLayout());
		panel = new Panel();
		panel.setCls("restriction_panel");
		panel.setHeight(150);
		panel.setAutoScroll(true);
		add(panel, new AnchorLayoutData("100% 100%"));
	}


	@Override
	protected void doOnResize(int width, int height) {
	    super.doOnResize(width, height);
	    panel.setHeight(getInnerHeight());
	    panel.setWidth(getInnerWidth());
	    doLayout();
	}


	@Override
	public void reload() {
		if (_currentEntity != null) {
			setTitle("Axioms for " + _currentEntity.getBrowserText());
			OntologyServiceManager.getInstance().getRestrictionHtml(project.getProjectName(),
	                _currentEntity.getName(), new GetRestrictionsHtmlHandler());
		} else {
		    setTitle("Axioms (nothing selected)");
		    panel.setHtml("");
		}

	}

	public ArrayList<EntityData> getSelection() {
		return new ArrayList<EntityData>();
	}

	/*
	 * RPC
	 */

	class GetRestrictionsHtmlHandler extends AbstractAsyncHandler<String> {

		@Override
		public void handleFailure(Throwable caught) {
			GWT.log("Error at getting restrictions", caught);
		}

		@Override
		public void handleSuccess(String html) {
			panel.setHtml(html);
		}
	}

}

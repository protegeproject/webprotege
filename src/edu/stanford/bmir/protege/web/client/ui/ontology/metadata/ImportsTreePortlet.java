package edu.stanford.bmir.protege.web.client.ui.ontology.metadata;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ImportsData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ImportsTreePortlet extends AbstractEntityPortlet {

	protected TreePanel importsTree;
	protected ArrayList<EntityData> currentSelection;

	public ImportsTreePortlet(Project project) {
		super(project);
	}

	public void reload() {
	}

	public void initialize() {
		setLayout(new FitLayout());
		setTitle("Imported Ontologies");

		importsTree = new TreePanel();
		importsTree.setHeight(400);
		importsTree.setAnimate(true);
		importsTree.setAutoWidth(true);		
		importsTree.setAutoScroll(true);

		importsTree.addListener(new TreePanelListenerAdapter() {
			public void onClick(TreeNode node, EventObject e) {
				currentSelection = new ArrayList<EntityData>();
				currentSelection.add((EntityData) node.getUserObject());
				notifySelectionListeners(new SelectionEvent(ImportsTreePortlet.this));
			}
		});
		
		String projectName = project.getProjectName();

		TreeNode root = new TreeNode(projectName);
		root.setText(projectName);
		root.setId(projectName);
		root.setUserObject(new EntityData(projectName, projectName));

		importsTree.setRootNode(root);
		importsTree.setRootVisible(true);

		add(importsTree);
	}

	protected void afterRender() {
		getImportedOntologies(project.getProjectName());
		super.afterRender();
	}

	public ArrayList<EntityData> getSelection() {
		return currentSelection;
	}

	public void getImportedOntologies(String projectName) {
		OntologyServiceManager.getInstance().getImportedOntologies(projectName,
				new GetImportedOntologies());
	}
	
	/*
	 * Remote calls
	 */

	class GetImportedOntologies extends AbstractAsyncHandler<ImportsData> {

		public void handleFailure(Throwable caught) {
			GWT.log("RPC error getting imported ontologies ", caught);
		}

		public void handleSuccess(ImportsData result) {
			TreeNode root = importsTree.getRootNode();
			addImports(result, root);
			root.select();
			root.expand();
		}
	}
	
	/*
	 * Helper methods
	 */
	
	private void addImports(ImportsData data, TreeNode node) {
		String nodeName = data.getName();
		node.setText(nodeName);
		node.setId(nodeName);
		node.setUserObject(new EntityData(data.getName(), data.getName()));
		
		ArrayList<ImportsData> imports = data.getImports();
		for (ImportsData importData : imports) {
			TreeNode childNode = new TreeNode();
			addImports(importData, childNode);
			node.appendChild(childNode);
		}
	}
}

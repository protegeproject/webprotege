package edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.tree.TreeNode;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.ChAOStatsServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * @author Sean Falconer <sean.falconer@stanford.edu>
 */
public class ChangeTreePortlet extends ClassTreePortlet {
	private ToolbarButton filterButton;

	private ToolbarButton refreshButton;

	private ChangeTreeFilterWindow changeTreeFilterWindow;

	private static final String FILTER_LABEL_PROP = "filter_label";
    private static final String FILTER_LABEL_DEFAULT = "Filter";

    private static final String REFRESH_LABEL_PROP = "refresh_label";
    private static final String REFRESH_LABEL_DEFAULT = "Refresh";

	public ChangeTreePortlet(Project project) {
		super(project, false, false, false, true, null);

		setTitle("Change tree");
		init();

		changeTreeFilterWindow = new ChangeTreeFilterWindow(project.getProjectName());
		changeTreeFilterWindow.addFilterListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				refreshViews(changeTreeFilterWindow.getDisplayedAuthors());
			}
		});
	}

	private void init() {
		setTopToolbar(new Toolbar());
        Toolbar toolbar = getTopToolbar();

        filterButton = new ToolbarButton(getFilterLabel());
        filterButton.setCls("toolbar-button");
        filterButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                filterViews();
            }
        });

        refreshButton = new ToolbarButton(getRefreshLabel());
        refreshButton.setCls("toolbar-button");
        refreshButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                refreshViewsWithAllAuthors();
            }
        });

        toolbar.addButton(filterButton);
        toolbar.addButton(refreshButton);
	}

	private void filterViews() {
		changeTreeFilterWindow.show();
	}

	private void refreshViewsWithAllAuthors() {
		ChAOStatsServiceManager.getInstance().getChangeAuthors(project.getProjectName(), new AsyncCallback<Collection<String>>() {
			public void onSuccess(Collection<String> authors) {
				List<String> authorList = new ArrayList<String>();
				for(String author : authors) {
					authorList.add(author);
				}
				refreshViews(authorList);
			}

			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void refreshViews(final List<String> authors) {
		ChAOStatsServiceManager.getInstance().applyChangeFilter(project.getProjectName(), authors, new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(Void arg0) {
				refreshCounts();

				ChangeTablePortlet changeTablePorlet = (ChangeTablePortlet)getTab().getPortletByClassName(ChangeTablePortlet.class.getName());

				((FilteringChangesProxyImpl)changeTablePorlet.getDataProxy()).setAuthors(authors);
				changeTablePorlet.reload();
			}
		});
	}

	protected String getFilterLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), FILTER_LABEL_PROP, FILTER_LABEL_DEFAULT);
    }

	protected String getRefreshLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), REFRESH_LABEL_PROP, REFRESH_LABEL_DEFAULT);
    }

	public void refreshCounts() {
		TreeNode root = getTreePanel().getRootNode();
		refreshCounts(root);
	}

	private void refreshCounts(final TreeNode node) {
		EntityData entityData = (EntityData)node.getUserObject();
		node.setText(UIUtil.getDisplayText(entityData));

		ChAOStatsServiceManager.getInstance().getNumChanges(project.getProjectName(), entityData.getName(), new AsyncCallback<Integer>() {
			public void onSuccess(Integer result) {
				if(result > 0) {
					node.setText(node.getText() + "<span style=\"font-weight:bold;\">(" + result + ")</span>");
				}
			}

			public void onFailure(Throwable caught) {
			}
		});

		ChAOStatsServiceManager.getInstance().getNumChildrenChanges(project.getProjectName(), entityData.getName(), new AsyncCallback<Integer>() {
			public void onSuccess(Integer result) {
				if(result > 0) {
					node.setText(node.getText() + "<span style=\"color:#999;\">(" + result + ")</span>");
				}
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

		for(Node child : node.getChildNodes()) {
            refreshCounts((TreeNode)child);
        }
	}

	@Override
	protected TreeNode createTreeNode(EntityData entityData) {
		final TreeNode node = new TreeNode(UIUtil.getDisplayText(entityData));
        node.setHref(null);
        node.setUserObject(entityData);
        node.setAllowDrag(true);
        node.setAllowDrop(true);
        setTreeNodeIcon(node, entityData);

        ChAOStatsServiceManager.getInstance().getNumChanges(project.getProjectName(), entityData.getName(), new AsyncCallback<Integer>() {
			public void onSuccess(Integer result) {
				if(result > 0) {
					node.setText(node.getText() + "<span style=\"font-weight:bold;\">(" + result + ")</span>");
				}
			}

			public void onFailure(Throwable caught) {
			}
		});

		ChAOStatsServiceManager.getInstance().getNumChildrenChanges(project.getProjectName(), entityData.getName(), new AsyncCallback<Integer>() {
			public void onSuccess(Integer result) {
				if(result > 0) {
					node.setText(node.getText() + "<span style=\"color:#999;\">(" + result + ")</span>");
				}
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

        return node;
	}
}

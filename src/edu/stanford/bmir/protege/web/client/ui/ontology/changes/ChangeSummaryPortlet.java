package edu.stanford.bmir.protege.web.client.ui.ontology.changes;

import java.util.Collection;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.AnchorLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 *
 */
public class ChangeSummaryPortlet extends AbstractEntityPortlet {

	private ChangesGrid grid;
	private Label lblToday;
	private Label lblYesterday;

	public ChangeSummaryPortlet(Project project) {
		super(project);
	}

	@Override
	public void initialize() {
		setLayout(new FitLayout());
		setBorder(true);
		setPaddings(15);

		setTitle("Change Summary for " + project.getProjectName());

		Panel main = new Panel();
		main.setLayout(new AnchorLayout());

		main.add(new HTML("<b>Recent Activity</b>"));

		// Number of changes for today
		FlowPanel today = new FlowPanel();
		today.add(new Label("Today: "));
		lblToday = new Label(" (refresh the widget to get the count)");
		today.add(lblToday);
		main.add(today);

		// Number of changes for yesterday
		FlowPanel yesterday = new FlowPanel();
		yesterday.add(new Label("Yesterday: "));
		lblYesterday = new Label(" (refresh the widget to get the count)");
		yesterday.add(lblYesterday);
		main.add(yesterday);

		main.add(new HTML("<br /><br />"));

		// Change history grid
		grid = new ChangesGrid(project);

		/*
		 * Didn't have time to investigate this much, but it was hard to get the
		 * grid to have good layout. When the grid is in an AnchorLayout, it
		 * seems that calls to setSize are ignored. Also, if you enter 100% for
		 * the height in the AnchorLayoutData, the grid draws outside the bottom
		 * of the containing portlet. I could only get this to work by
		 * specifying a negative pixel value for the height.
		 */
		main.add(grid, new AnchorLayoutData("100% -75"));
		add(main);
	}

	public Collection<EntityData> getSelection() {
		return null;
	}

	@Override
	public void reload() {
		// onRefresh(); //FIXME
	}

	@Override
	protected void afterRender() {
		// onRefresh();
	}

	@Override
	protected void onRefresh() {
	    refreshContent();
	}

	@SuppressWarnings("deprecation")
	protected void refreshContent() {

		/*
		 * Tried using the non-deprecated methods here (i.e., from Calendar),
		 * but this results in GWT error: "No source code is available for type
		 * java.util.Calendar; did you forget to inherit a required module?".
		 * Apparently, the Calendar class is not in the GWT 1.5 JRE emulation
		 * library and can't be used on the client-side. Check available classes
		 * for client-side code here: http://tinyurl.com/bwrden.
		 */

		Date start = new Date();
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);

		Date end = new Date();
		end.setDate(end.getDate() + 1);
		end.setHours(0);
		end.setMinutes(0);
		end.setSeconds(0);

		ChAOServiceManager.getInstance().getNumChanges(
				project.getProjectName(), start, end,
				new GetNumChangesHandler(0));

		start.setDate(start.getDate() - 1);
		end.setDate(end.getDate() - 1);

		ChAOServiceManager.getInstance().getNumChanges(
				project.getProjectName(), start, end,
				new GetNumChangesHandler(-1));

		grid.reload();
	}

	@Override
	public void onLogin(String userName) {
		// onRefresh();
	}

	@Override
	public void onLogout(String userName) {
		// onRefresh();
	}

	/*
	 * Remote calls
	 */

	class GetNumChangesHandler extends AbstractAsyncHandler<Integer> {
		private int labelType; // 0 - today, -1 - yesterday

		public GetNumChangesHandler(int labelType) {
			this.labelType = labelType;
		}

		@Override
		public void handleFailure(Throwable caught) {
			GWT.log("RPC error getting number of changes for the "
					+ project.getProjectName() + "ontology", caught);
		}

		@Override
		public void handleSuccess(Integer numChanges) {
			if (numChanges == null || numChanges.equals(0)) {
				setLabelText("no changes detected");
			} else {
				setLabelText(numChanges.toString());
			}
		}

		private void setLabelText(String text) {
			if (labelType == 0) { // today
				lblToday.setText(text);
			} else if (labelType == -1) { // yesterday
				lblYesterday.setText(text);
			}
		}
	}
}

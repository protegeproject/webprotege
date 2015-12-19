package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;

public class ClassSelectionFieldWidget extends AbstractSelectionFieldWidget {

	public ClassSelectionFieldWidget(Project project, DispatchServiceManager dispatchServiceManager) {
		super(project, dispatchServiceManager);
	}

	
	public Selectable createSelectable() {
		throw new RuntimeException("Implementation broken");
//		ClassTreePortlet classTreePortlet = new ClassTreePortlet(getProject());
//		classTreePortlet.setHeight(250);
//		classTreePortlet.setWidth(200);
//		return classTreePortlet;
	}
}

package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.HtmlEditor;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;

public class HTMLEditorWidget extends AbstractFieldWidget {

	public HTMLEditorWidget(Project project, DispatchServiceManager dispatchServiceManager) {
		super(project, dispatchServiceManager);
	}
	
	@Override
	protected Field createFieldComponent() {	
		HtmlEditor htmlEditor = new HtmlEditor();
		htmlEditor.setCtCls("form_html_panel");
	
		return htmlEditor;
	}

}

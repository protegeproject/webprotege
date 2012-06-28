package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.HtmlEditor;

import edu.stanford.bmir.protege.web.client.model.Project;

public class HTMLEditorWidget extends AbstractFieldWidget {

	public HTMLEditorWidget(Project project) {
		super(project);	
	}
	
	@Override
	protected Field createFieldComponent() {	
		HtmlEditor htmlEditor = new HtmlEditor();
		htmlEditor.setCtCls("form_html_panel");
	
		return htmlEditor;
	}

}

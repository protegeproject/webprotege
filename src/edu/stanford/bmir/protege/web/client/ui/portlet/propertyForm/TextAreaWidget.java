package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;

import edu.stanford.bmir.protege.web.client.model.Project;


public class TextAreaWidget extends AbstractFieldWidget {

	public TextAreaWidget(Project project) {
		super(project);	
	}
	
	 @Override
	protected TextField createFieldComponent() {
		 return new TextArea();
	}

}

package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.gwtext.client.widgets.Component;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractPropertyWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.html.HtmlTextComponent;

public class HtmlMessageWidget extends AbstractPropertyWidget {

	private HtmlTextComponent htmlTextComponent;

    public HtmlMessageWidget(Project project) {
		super(project);		
	}

    @Override
    public Component createComponent() {
        htmlTextComponent = new HtmlTextComponent(getProject());
        htmlTextComponent.setConfigProperties(getWidgetConfiguration());
        return htmlTextComponent;
    }

    @Override
    public Component getComponent() {
        return htmlTextComponent;
    }

}

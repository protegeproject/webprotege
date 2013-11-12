package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Component;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractPropertyWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.html.HtmlTextComponent;

public class HtmlMessageWidget extends AbstractPropertyWidget {

	private HtmlTextComponent htmlTextComponent;

    public HtmlMessageWidget(Project project) {
		super(project);		
	}

    @Override
    public Widget createComponent() {
        htmlTextComponent = new HtmlTextComponent(getProject());
        htmlTextComponent.setConfigProperties(getWidgetConfiguration());
        return htmlTextComponent;
    }

    @Override
    public Widget getComponent() {
        return htmlTextComponent;
    }

}

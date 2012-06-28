package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractPropertyWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.PropertyWidget;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class TextFieldMultiWidget extends AbstractPropertyWidget {

	private Panel wrappingPanel;
	private final List<PropertyWidget> widgets;
	private String labelString;
	private Anchor addNewLink;

	private Collection<EntityData> values;

	public TextFieldMultiWidget(Project project) {
		super(project);
		widgets = new ArrayList<PropertyWidget>();
		}

	@Override
	public Component createComponent() {
	    wrappingPanel = new Panel();
        addNewLink = new Anchor("&nbsp&nbsp<img src=\"images/add.png\"></img>&nbsp Add new value", true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (!isReadOnly() && !isDisabled()) {
                    onAddNewValue();
                }
            }
        });
        return wrappingPanel;
     }

	@Override
    public void refresh() {
		wrappingPanel.doLayout();
	}

	protected void onAddNewValue() {
		PropertyWidget widget = createWidget("");
		//TODO: fix me!!!!! hack!! not cast to component
		wrappingPanel.insert(wrappingPanel.getComponents().length -1, widget.getComponent());

		wrappingPanel.doLayout();
	}

	protected Field createFieldComponent() {
		TextField textField = new TextField();
		return textField;
	}

	@Override
	public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData property) {
		super.setup(widgetConfiguration, property);
		initLabel((String) widgetConfiguration.get(FormConstants.LABEL));
	}

	protected void initLabel(String text) {
		labelString = text;
		//this.label.setText(text);
		//addNewLink.setTitle("Add new value for " + getProperty().getName());
	}


	@Override
	public Collection<EntityData> getValues() {
		//TODO: implement this
		return new ArrayList<EntityData>();
	}

	@Override
	public void setProperty(PropertyEntityData property) {
		super.setProperty(property);
		if (addNewLink != null) {
		    addNewLink.setTitle("Add new value for " + UIUtil.getDisplayText(property));
		}
	}


	@Override
    public void setValues(Collection<EntityData> vs) {
		this.values = vs;
		wrappingPanel.removeAll();
		if (values.size() > 0)  {
			for (EntityData value : values) {
				PropertyWidget widget = createWidget(value);
				widgets.add(widget);
				add(widget);
				List<EntityData> vals = new ArrayList<EntityData>();
				vals.add(value);
				widget.setValues(vals);
			}
		} else {
				add(createWidget(""));
		}
		if (!isDisabled() && !isReadOnly()) {
		    wrappingPanel.add(addNewLink);
		}
		wrappingPanel.doLayout();
	}

	private PropertyWidget createWidget(Object value) {
	    TextFieldWidget widget = createInnerWidget();

		if (wrappingPanel.getComponents().length == 0) { //TODO: hack
			widget.setLabel(labelString, getHelpURL(), getTooltipText());
		} else {
			widget.setLabel(null, null, null); //hack
			widget.getField().setLabelSeparator("");
		}
		widget.setSubject(getSubject());
		widget.setProperty(getProperty());
		return widget;
	}

	private TextFieldWidget createInnerWidget() {
	    TextFieldWidget widget = new TextFieldWidget(getProject());

	    HashMap<String, Object> widgetConfig = new HashMap<String, Object>();
	    widget.setWidgetConfiguration(widgetConfig); //TODO: check what else to put in the widget config
        widget.setReadOnly(isReadOnly());
        widget.setDisabled( isDisabled());

        widget.setup(widgetConfig, getProperty());
        return widget;
	}

	private void add(PropertyWidget widget) {
		wrappingPanel.add(widget.getComponent());
	}


	@Override
	public Component getComponent() {
		return wrappingPanel;
	}
}

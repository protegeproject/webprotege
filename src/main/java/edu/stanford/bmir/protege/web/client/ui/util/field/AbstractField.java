package edu.stanford.bmir.protege.web.client.ui.util.field;

import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

public class AbstractField extends Panel {

    private Component labelComponent;
    private Component fieldComponent;
    private Label label;

    public AbstractField() {
        super();
        setLayout(new ColumnLayout());

        labelComponent = createLabelComponent();
        labelComponent.setCls(getLabelComponentCls());
        fieldComponent = createFieldComponent();
        fieldComponent.setCls(getFieldComponentCls());

        add(labelComponent);
        add(fieldComponent, new ColumnLayoutData(1));
    }

    protected Component createLabelComponent() {
        label = new Label();
        return label;
    }

    protected Component createFieldComponent() {
        return new Panel();
    }

    protected String getLabelComponentCls() {
        return "parents-label";
    }

    protected String getFieldComponentCls() {
        return "parents";
    }

    public Component getLabelComponent() {
        return labelComponent;
    }

    public Component getFieldComponent() {
        return fieldComponent;
    }

    public String getLabel() {
        return label != null ? label.getText() : "";
    }

    public void setLabel(String label) {
        if (label != null) {
            this.label.setText(label);
        }
    }

}

package edu.stanford.bmir.protege.web.client.ui.projectconfig;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdGeneratorSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdUserRange;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
public class EntityIdGeneratorSettingsForm extends FlowPanel {

    public static final int DEFAULT_ID_LENGTH = 7;

    private final TextBox baseField;

    private final TextBox idPrefixField;

    private final TextBox idLengthField;

    private final TextBox idStartField;

    private final FlexTable userRangePanel;

    private final InlineLabel exampleLabel;

    public EntityIdGeneratorSettingsForm() {
        WebProtegeDialogForm form = new WebProtegeDialogForm();
        add(form);
        baseField = new TextBox();
        baseField.setVisibleLength(50);
        
        form.addWidget("Base", baseField);
        idPrefixField = new TextBox();
        idPrefixField.setVisibleLength(10);
        form.addWidget("Id prefix", idPrefixField);
        idLengthField = new TextBox();
        form.addWidget("Id length", idLengthField);
        idStartField = new TextBox();
        form.addWidget("Id start", idStartField);

        form.addVerticalSpacer();

        exampleLabel = new InlineLabel();
        form.addWidget("Example", exampleLabel);
        
        form.addVerticalSpacer();
        
        userRangePanel = new FlexTable();
        form.addWidget("User ranges", userRangePanel);


        ChangeHandler exampleChangeHandler = new ChangeHandler() {
            public void onChange(ChangeEvent event) {
                updateExample();
            }
        };
        baseField.addChangeHandler(exampleChangeHandler);
        idPrefixField.addChangeHandler(exampleChangeHandler);
        idLengthField.addChangeHandler(exampleChangeHandler);
        idStartField.addChangeHandler(exampleChangeHandler);

    }


    private void updateExample() {
        StringBuilder exampleIdBuilder = new StringBuilder();
        for(int i = 0; i < getIdLength() - 3; i++) {
            exampleIdBuilder.append("0");
        }
        int startId = getIdStart();
        if(startId == 0) {
            startId = 347;
        }
        exampleIdBuilder.append(startId);
        exampleLabel.setText(getIdBase() + getIdPrefix() + "_" + exampleIdBuilder.toString());
    }

    public Focusable getInitialFocusable() {
        return baseField;
    }
    
    public void setData(EntityIdGeneratorSettings data) {
        baseField.setText(data.getBase());
        idPrefixField.setText(data.getIdPrefix());
        idLengthField.setText(Integer.toString(data.getIdLength()));
        idStartField.setText("0");

        userRangePanel.removeAllRows();
        for(EntityIdUserRange range : data.getUserRanges()) {
            int rowCount = userRangePanel.getRowCount();
            userRangePanel.setWidget(rowCount, 0, new EntityIdGeneratorUserRangePanel(range));
        }
        userRangePanel.setWidget(userRangePanel.getRowCount(), 0, new InlineLabel("(Use sharing settings to add more users)"));
        updateExample();
    }
    
    public EntityIdGeneratorSettings getData() {
        List<EntityIdUserRange> ranges = getEntityIdUserRanges();
        return new EntityIdGeneratorSettings(getIdBase(), getIdPrefix(), getIdLength(), ranges);
    }

    private List<EntityIdUserRange> getEntityIdUserRanges() {
        List<EntityIdUserRange> ranges = new ArrayList<EntityIdUserRange>();
        for(int i = 0; i < userRangePanel.getRowCount(); i++) {
            Widget w = userRangePanel.getWidget(i, 0);
            if(w instanceof EntityIdGeneratorUserRangePanel) {
                EntityIdUserRange rng = ((EntityIdGeneratorUserRangePanel) w).getData();
                ranges.add(rng);
            }
        }
        return ranges;
    }

    private int getIdLength() {
        try {
            return Integer.parseInt(idLengthField.getText().trim());
        }
        catch (NumberFormatException e){
            return DEFAULT_ID_LENGTH;
        }
    }

    private String getIdPrefix() {
        return idPrefixField.getText().trim();
    }
    
    private int getIdStart() {
        try{
            return Integer.parseInt(idStartField.getText().trim());
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    private String getIdBase() {
        String rawBase = baseField.getText().trim().replaceAll("\\s+", "-");
        if(rawBase.endsWith("#")) {
            return rawBase;
        }
        if(rawBase.endsWith("/")) {
            return rawBase;
        }
        return rawBase + "/";
    }
}

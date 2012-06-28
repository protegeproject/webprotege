package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/01/2012
 */
public class WebProtegeDialogForm extends WebProtegePanel {

    /**
     * The column that holds the widgets.
     */
    public static final int LABEL_COLUMN = 0;

    /**
     * The column that holds the labels
     */
    public static final int WIDGET_COLUMN = 1;

    public static final String LABEL_SUFFIX = ":";


    private final FlexTable formTable = new FlexTable();

    private final FormPanel formPanel;
    
    private List<WebProtegeDialogValidator> validators = new ArrayList<WebProtegeDialogValidator>();

    public WebProtegeDialogForm() {
        formPanel = new FormPanel();
        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        SimplePanel formTableWrapper = new SimplePanel();
        formPanel.setWidget(formTableWrapper);
        formTableWrapper.setWidget(formTable);
        add(formPanel);
        formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
            }
        });
    }

    public void addDialogValidator(WebProtegeDialogValidator validator) {
        validators.add(validator);
    }

    public List<WebProtegeDialogValidator> getDialogValidators() {
        return new ArrayList<WebProtegeDialogValidator>(validators);
    }

    public void submit() {
        formPanel.submit();
    }

    public void addSubmitCompleteHandler(FormPanel.SubmitCompleteHandler handler) {
        formPanel.addSubmitCompleteHandler(handler);
    }
    
    public void addSubmitHandler(FormPanel.SubmitHandler submitHandler) {
        formPanel.addSubmitHandler(submitHandler);
    }
    
    public void setPostURL(String url) {
        formPanel.setAction(url);
    }
    
    public void setMultipartEncoding(boolean b) {
        if(b) {
            formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        }
        else {
            formPanel.setEncoding(FormPanel.ENCODING_URLENCODED);
        }
    }

    public void addWidget(String label, Widget widget) {
        int insertionRow = formTable.getRowCount();
        String labelText = getLabelText(label);
        if (!label.isEmpty()) {
            formTable.setWidget(insertionRow, LABEL_COLUMN, new WebProtegeDialogLabel(labelText));
        }
        formTable.setWidget(insertionRow, WIDGET_COLUMN, widget);
        formTable.getCellFormatter().setVerticalAlignment(insertionRow, LABEL_COLUMN, HasVerticalAlignment.ALIGN_TOP);
        if(insertionRow == 0 && widget instanceof Focusable) {
            ((Focusable) widget).setFocus(true);
        }
        formTable.setCellSpacing(5);
    }

    public void addVerticalSpacer() {
        int insertionRow = formTable.getRowCount();
        SimplePanel labelSpacer = createSpacer();
        formTable.setWidget(insertionRow, LABEL_COLUMN, labelSpacer);
    }

    private static SimplePanel createSpacer() {
        SimplePanel spacer = new SimplePanel();
        spacer.addStyleName("web-protege-vertical-spacer");
        return spacer;
    }

    /**
     * Gets the text for a label on the form.
     * @param label The user requested label.
     * @return The label text with a suffix determined by the {@link #LABEL_SUFFIX} field value.
     */
    private String getLabelText(String label) {
        return label.endsWith(LABEL_SUFFIX) ? label : label + LABEL_SUFFIX;
    }
}

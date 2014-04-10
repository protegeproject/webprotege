package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.HtmlEditor;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.layout.FitLayout;

public class PopupGridEditor {

    private Window getPopupWindow(String propName, String indivPropName, String value, boolean editable) {
        if (popupWindow == null) {
            popupWindow = new Window();
            popupWindow.setMinWidth(400);
            popupWindow.setMinHeight(200);
            popupWindow.setSize(600,350);
            
            popupWindow.setModal(true);
            popupWindow.setLayout(new FitLayout());
            popupWindow.add(field);
            field.setTabIndex(0);
            
            toolbar = new Toolbar();
            toolbar.addText("<span style=\"color:#CC0000;font-weight:bold;\">This is a read-only view. Changes will not be saved!</span>");
            popupWindow.setBottomToolbar(toolbar);
            
            popupWindow.setButtonAlign(Position.CENTER);
            okButton = new Button("Close", new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    onCancel(field);
                }
            });
            saveButton = new Button("Save", new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    onConfirm(field);
                }
            });
            cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    onCancel(field);
                }
            });
            popupWindow.addButton(okButton);
            popupWindow.addButton(saveButton);
            popupWindow.addButton(cancelButton);
            
            popupWindow.addListener(new WindowListenerAdapter() {
                @Override
                public boolean doBeforeClose(Panel panel) {
                    onCancel(field);
//                    popupWindow.hide();
                    return false;
                }
                
                @Override
                public void onHide(Component panel) {
                    if (hasValueChanged()) {
                        getCallbackFunction().execute();
                    }
                }
                
                @Override
                public void onShow(Component component) {
                   
                   Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                         public void execute() {
                             System.out.println("Deferred!!!! Is field rendered:" + field.isRendered());
                             new Timer() {
                                 @Override
                                 public void run() {
                                     System.out.println("Timer running:");
                                     field.focus();
                                 }
                                }.schedule(100);
                         }
                     });
                }
            });

        }
        popupWindow.setTitle("'" + indivPropName + "' of the '" + propName + "' property value");
        this.field.setValue(value);
        if (editable) {
            toolbar.setVisible(false);
            okButton.hide();
            saveButton.show();
            cancelButton.show();
        }
        else {
            toolbar.setVisible(true);
            okButton.show();
            saveButton.hide();
            cancelButton.hide();
        }
        
        this.editable = editable;
        this.field.setReadOnly(!editable);
        
        //TODO Implement a workaround for the bug that exist in the extjs and prevents HTMLEditor to be read-only!!!!
//        if (field instanceof HtmlEditor) {
//            ((HtmlEditor)field).setReadOnly(!editable);
//            
//            //((HtmlEditor)field).getElement().getChild(0).
//        }

        oldValue = value;
        newValue = null;
        valueChanged = false;
        
        return popupWindow;
    }
    

    private void onConfirm(final Field field) {
        newValue = field.getValueAsString();
        if (hasValueChanged(oldValue, newValue)) {
            valueChanged = true;
        }
        popupWindow.hide();
    }

    private void onCancel(final Field field) {
        newValue = field.getValueAsString();
        if (editable && hasValueChanged(oldValue, newValue)) {
            System.out.println("old value: " + oldValue + "\nnew value: " + newValue);
            MessageBox.confirm("Discard changes?", "You are about to discard the changes that you have made.<BR>" +
                    "If you want to discard these changes, click 'Yes'. " + "Otherwise, click 'No' to continue editing.", 
                    new MessageBox.ConfirmCallback() {
                        public void execute(String btnID) {
                            if (btnID.toLowerCase().equals("yes")) {
                                popupWindow.hide();
                            }
                            //else do nothing, i.e. continue editing
                        }
                    });
        }
        else {
            popupWindow.hide();
        }
    } 

    
    static public final PopupGridEditor HTML = new PopupGridEditor(new HtmlEditor());
    static public final PopupGridEditor TEXT_AREA = new PopupGridEditor(new TextArea());
    
    private Field field;
    private Function cbFunction;
    private String oldValue;
    private String newValue;
    private boolean valueChanged;
    private Window popupWindow = null;
    private Toolbar toolbar;
    private Button okButton = null;
    private Button saveButton = null;
    private Button cancelButton = null;
    private boolean editable;
    
    public PopupGridEditor(Field field) {
        this.field = field;
    }

    public void setCallbackFunction(Function cbFunction) {
        this.cbFunction = cbFunction;
    }
    
    public Function getCallbackFunction() {
        return cbFunction;
    }
    
    public void show(String propName, String indivPropName, String value, boolean editable) {
        popupWindow = getPopupWindow(propName, indivPropName, value, editable);
        popupWindow.show();
    }

    
    
    public boolean hasValueChanged(String oldValue, String newValue) {
        if (oldValue == null || oldValue.isEmpty()) {
            if (newValue == null 
                    || newValue.trim().isEmpty() 
                    || newValue.trim().toLowerCase().equals("<br>")
                    || newValue.trim().toLowerCase().equals("&nbsp;")) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return (! oldValue.equals(newValue));
        }
    }    
    
    public boolean hasValueChanged() {
        System.out.println("returning value changed: " + valueChanged);
        return valueChanged;
    }

    public String getValue() {
        return newValue;
    }
}

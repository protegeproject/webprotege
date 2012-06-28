package edu.stanford.bmir.protege.web.client.ui.ontology.reviews;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.HtmlEditor;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class AddReviewWindow extends Window {

    private AsyncCallback<NotesData> cb;
	private HtmlEditor htmlEditor;
	private TextField subjectField;

	public AddReviewWindow(AsyncCallback<NotesData> cb) {
		this.cb = cb;
		initialize();
	}

	private void initialize() {
        setTitle("Add Review");
        setWidth(600);
        setHeight(400);
        setLayout(new FitLayout());
        setPaddings(5);
        setButtonAlign(Position.CENTER);
        setCloseAction(Window.HIDE);
        setPlain(true);

        Button okButton = new Button("OK", new ButtonListenerAdapter() {
        	@Override
            public void onClick(Button button, EventObject e) {
        		okButton_onClick(button, e);
			}
        });
        addButton(okButton);

        Button cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
        	@Override
            public void onClick(Button button, EventObject e) {
        		cancelButton_onClick(button, e);
			}
        });
        addButton(cancelButton);

        FormPanel formPanel = new FormPanel();
        formPanel.setBaseCls("x-plain");
        formPanel.setLabelWidth(55);
        formPanel.setWidth(500);
        formPanel.setHeight(300);

        subjectField = new TextField("Subject", "subject");
        formPanel.add(subjectField, new AnchorLayoutData("100%"));

        htmlEditor = new HtmlEditor("Body", "body");
        htmlEditor.setHideLabel(true);
        formPanel.add(htmlEditor, new AnchorLayoutData("100% -33"));

        add(formPanel);
	}

	private void cancelButton_onClick(Button button, EventObject e) {
		close();
    }

	private void okButton_onClick(Button button, EventObject e) {
		// Shortcut
		if (subjectField.getText().equals("")) {
			MessageBox.alert("Please enter a subject");
			return;
		}

		NotesData data = new NotesData();
		data.setAuthor(GlobalSettings.getGlobalSettings().getUserName());
		data.setSubject(subjectField.getText());
		data.setBody(htmlEditor.getValueAsString());
		Date date = new Date();
		data.setCreationDate(date.toString());
		cb.onSuccess(data);

		close();
    }
}

package edu.stanford.bmir.protege.web.client.ui.portlet;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorDialog;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.NoteContentEditorMode;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityAction;
import edu.stanford.bmir.protege.web.shared.notes.AddNoteToEntityResult;
import edu.stanford.bmir.protege.web.shared.notes.NoteContent;
import org.semanticweb.owlapi.model.OWLEntity;

public abstract class AbstractPropertyWidgetWithNotes extends AbstractPropertyWidget {

    public static final String COMMENT_ICON_STYLE_STRING = "style=\"position:relative; top:2px;\"";

    public AbstractPropertyWidgetWithNotes(Project project) {
        super(project);
    }

    protected void onEditNotes(String value) {
        onEditNotes(value, "", "");
    }

    protected void onEditNotes(String value, String subject, String text) {
        onEditNotes(value, "Please enter your note:", subject, text);
    }

    protected void onEditNotes(final String value, String message, String subject, String text) {
        if (value != null) {
            GWT.log("Editing notes on " + value + " ...", null);

            final Window window = new Window() {
                @Override
                public void close() {
                    AbstractPropertyWidgetWithNotes.this.refresh();
                    super.close();
                }
            };
            window.setTitle("Edit Notes");
            window.setWidth(600);
            window.setHeight(480);
            window.setMinWidth(300);
            window.setMinHeight(350);
            window.setLayout(new FitLayout());
            window.setPaddings(5);
            window.setButtonAlign(Position.CENTER);

            //window.setCloseAction(Window.HIDE);
            window.setPlain(true);

            // TODO: THIS NEEDS FIXING
            NoteContentEditorDialog.showDialog(NoteContentEditorMode.NEW_TOPIC, new NoteContentEditorHandler() {
                @Override
                public void handleAccept(Optional<NoteContent> noteContent) {
                    if (noteContent.isPresent()) {
                        OWLEntity entity = DataFactory.getOWLClass(value);
                        DispatchServiceManager.get().execute(new AddNoteToEntityAction(getProjectId(), entity, noteContent.get()), new AsyncCallback<AddNoteToEntityResult>() {
                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(AddNoteToEntityResult result) {
                            }
                        });
                    }
                }
            });
//            NoteInputPanel nip = new NoteInputPanel(getProjectId(), message, true,
//                    subject, text, new EntityData(value), window);
//
//            window.add(nip);
            window.show();
        }
    }

}

package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.client.ui.notes.DiscussionThreadPresenter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class DiscussionThreadDialog extends WebProtegeDialog<Void> {



    private DiscussionThreadDialog(String title, final Widget widget) {
        super(new WebProtegeOKDialogController<Void>(title) {
            @Override
            public Widget getWidget() {
                return widget;
            }

            @Override
            public Focusable getInitialFocusable() {
                return null;
            }

            @Override
            public Void getData() {
                return null;
            }
        });
    }





    public static void showDialog(ProjectId projectId, OWLEntity target) {
        final DiscussionThreadPresenter presenter = new DiscussionThreadPresenter(projectId);
        presenter.setTarget(target);
        DiscussionThreadDialog dlg = new DiscussionThreadDialog("Discussions", presenter.getWidget());
        dlg.setWidth("600px");
        dlg.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Void>() {
            @Override
            public void handleHide(Void data, WebProtegeDialogCloser closer) {
                presenter.dispose();
                closer.hide();
            }
        });
        dlg.setVisible(true);
    }

}

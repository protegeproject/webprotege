package edu.stanford.bmir.protege.web.client.ui.notes.editor;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.client.ui.notes.DiscussionThreadPresenter;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.inject.Inject;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/04/2013
 */
public class DiscussionThreadDialog {


    private static final String WIDTH_PX = "600px";

    private static final String HEIGHT_PX = "500px";

    private final DiscussionThreadPresenter presenter;

    @Inject
    public DiscussionThreadDialog(DiscussionThreadPresenter presenter) {
        this.presenter = presenter;
    }

    public void showDialog(OWLEntity target) {
        presenter.setTarget(target);
        WebProtegeOKCancelDialogController<Void> controller = new WebProtegeOKCancelDialogController<Void>("Discussions") {
            @Override
            public Widget getWidget() {
                return presenter.getWidget();
            }

            @Override
            public Optional<Focusable> getInitialFocusable() {
                return Optional.absent();
            }

            @Override
            public Void getData() {
                return null;
            }
        };
        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Void>() {
            @Override
            public void handleHide(Void data, WebProtegeDialogCloser closer) {
                presenter.dispose();
                closer.hide();
            }
        });
        WebProtegeDialog.showDialog(controller);
    }

//    public static void showDialog(ProjectId projectId, EventBus eventBus, DispatchServiceManager dispatchServiceManager, OWLEntity target) {
//        final DiscussionThreadPresenter presenter = new DiscussionThreadPresenter(projectId, eventBus, dispatchServiceManager, loggedInUse);
//        presenter.setTarget(target);
//        final Widget widget = presenter.getWidget();
//        widget.setSize(WIDTH_PX, HEIGHT_PX);
//        WebProtegeOKCancelDialogController<Void> controller = new WebProtegeOKCancelDialogController<Void>("Discussions") {
//            @Override
//            public Widget getWidget() {
//                return widget;
//            }
//
//            @Override
//            public Optional<Focusable> getInitialFocusable() {
//                return Optional.absent();
//            }
//
//            @Override
//            public Void getData() {
//                return null;
//            }
//        };
//        controller.setDialogButtonHandler(DialogButton.OK, new WebProtegeDialogButtonHandler<Void>() {
//            @Override
//            public void handleHide(Void data, WebProtegeDialogCloser closer) {
//                presenter.dispose();
//                closer.hide();
//            }
//        });
//        WebProtegeDialog.showDialog(controller);
//    }

}

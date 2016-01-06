package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.ui.library.progress.ProgressMonitor;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class ProgressDisplayImpl implements ProgressDisplay {

    @Override
    public void displayProgress(String title, String message) {
        ProgressMonitor.get().showProgressMonitor(title, message);
    }

    @Override
    public void hideProgress() {
        ProgressMonitor.get().hideProgressMonitor();
    }
}

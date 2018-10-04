package edu.stanford.bmir.protege.web.client.dispatch;

import edu.stanford.bmir.protege.web.client.progress.ProgressMonitor;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public class ProgressDisplayImpl implements ProgressDisplay {

    @Inject
    public ProgressDisplayImpl() {
    }

    @Override
    public void displayProgress(String title, String message) {
        ProgressMonitor.get().showProgressMonitor(title, message);
    }

    @Override
    public void hideProgress() {
        ProgressMonitor.get().hideProgressMonitor();
    }
}

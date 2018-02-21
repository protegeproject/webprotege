package edu.stanford.bmir.protege.web.client.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public interface ProgressDisplay {

    void displayProgress(String title, String message);

    void hideProgress();
}

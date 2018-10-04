package edu.stanford.bmir.protege.web.client.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/02/15
 */
public abstract class DispatchServiceCallbackWithProgressDisplay<T> extends DispatchServiceCallback<T> {

    private ProgressDisplay progressDisplay;


    public DispatchServiceCallbackWithProgressDisplay(DispatchErrorMessageDisplay errorMessageDisplay, ProgressDisplay progressDisplay) {
        super(errorMessageDisplay);
        this.progressDisplay = progressDisplay;
    }

    @Override
    public final void handleSubmittedForExecution() {
        String title = getProgressDisplayTitle();
        String message = getProgressDisplayMessage();
        progressDisplay.displayProgress(title, message);
    }

    public abstract String getProgressDisplayTitle();

    public abstract String getProgressDisplayMessage();

    @Override
    public void handleFinally() {
        progressDisplay.hideProgress();
        handleProgressDisplayFinally();
    }

    /**
     * Called after {@link #handleFinally()} and after the progress display has been hidden.
     */
    public void handleProgressDisplayFinally() {

    }
}

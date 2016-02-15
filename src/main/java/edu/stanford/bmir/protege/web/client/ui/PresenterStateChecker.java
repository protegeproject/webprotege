package edu.stanford.bmir.protege.web.client.ui;
/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PresenterStateChecker {

    private enum  PresenterState {

        STOPPED,

        STARTED
    }

    private PresenterState presenterState = PresenterState.STOPPED;


    public void start() {
        if(presenterState == PresenterState.STARTED) {
            throw new IllegalStateException("Presenter is already started");
        }
        presenterState = PresenterState.STARTED;
    }

    public void stop() {
        if(presenterState == PresenterState.STOPPED) {
            throw new IllegalStateException("Presenter is already stopped");
        }
        presenterState = PresenterState.STOPPED;
    }

    public void onStartedStateOperation() {
        if(presenterState == PresenterState.STOPPED) {
            throw new IllegalStateException("Presenter is stopped");
        }
    }

}
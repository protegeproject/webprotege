package edu.stanford.bmir.protege.web.client.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import edu.stanford.bmir.protege.web.client.model.event.*;
import edu.stanford.bmir.protege.web.client.model.listener.OntologyListener;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;

/**
 * Polls the server for events every x seconds. Keeps the current version of the
 * project on the client. Dispatches events to the listeners.
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class OntologyEventManager {

    /**
     * Sets the time interval in seconds in which the client will periodically
     * ask the server for new events. If SERVER_POLL_DELAY = -1, then the client
     * will not poll for events.
     */
    private static int SERVER_POLL_DELAY = 10;

    private static int ERROR_REPORTING_THRESHOLD = 10;

    private int errorsFromServer = 0;

    private boolean isTimerInitialized = false;

    private Project project;

    private int revision;

    private Timer getEventsTimer;

    private List<OntologyEvent> events;

    private List<OntologyListener> ontologyListeners;


    public OntologyEventManager(Project project) {
        this.project = project;
        this.ontologyListeners = new ArrayList<OntologyListener>();
        this.events = new ArrayList<OntologyEvent>();
    }

    /**
     * Should happen only at init
     * @param serverVersion
     */
    public void setServerVersion(int serverVersion) {
        revision = serverVersion;
    }

    public void addOntologyListener(OntologyListener ontologyListener) {
        ontologyListeners.add(ontologyListener);
        if (!isTimerInitialized) {
            startGetEventsTimer(SERVER_POLL_DELAY);
            isTimerInitialized = true;
        }
    }

    public void removeOntologyListener(OntologyListener ontologyListener) {
        ontologyListeners.remove(ontologyListener);
    }

    private void dispatchEvents(List<OntologyEvent> events) {
        for (Iterator<OntologyEvent> iterator = events.iterator(); iterator.hasNext(); ) {
            OntologyEvent event = iterator.next();
            dispatchEvent(event);
            iterator.remove();
        }
    }

    private void dispatchEvent(OntologyEvent event) {
        for (OntologyListener ontologyListener : ontologyListeners) {
            OntologyListener listener = ontologyListener;
            try {
                if (event instanceof EntityCreateEvent) {
                    listener.entityCreated((EntityCreateEvent) event);
                }
                else if (event instanceof EntityDeleteEvent) {
                    listener.entityDeleted((EntityDeleteEvent) event);
                }
                else if (event instanceof EntityRenameEvent) {
                    listener.entityRenamed((EntityRenameEvent) event);
                }
                else if (event instanceof PropertyValueEvent) {
                    PropertyValueEvent pve = (PropertyValueEvent) event;
                    if (pve.getEventType() == EventType.PROPERTY_VALUE_ADDED) {
                        listener.propertyValueAdded(pve);
                    }
                    else if (pve.getEventType() == EventType.PROPERTY_VALUE_REMOVED) {
                        listener.propertyValueRemoved(pve);
                    }
                    else if (pve.getEventType() == EventType.PROPERTY_VALUE_CHANGED) {
                        listener.propertyValueChanged(pve);
                    }
                }
                else if (event.getType() == EventType.INDIVIDUAL_ADDED_OR_REMOVED) {
                    listener.individualAddedRemoved(event);
                }
                else if(event instanceof EntityBrowserTextChangedEvent) {
                    listener.entityBrowserTextChanged((EntityBrowserTextChangedEvent) event);
                }
                else {
                    GWT.log("Unknown type of event: " + event, null);
                }
            }
            catch (Exception e) {
                GWT.log("Failed at event dispatch " + event, e);
            }
        }
    }

    /*
     * Timer methods
     */
    private void startGetEventsTimer(int seconds) {
        if (getEventsTimer != null) {
            stopGetEventsTimer();
        }
        if (seconds > 0) {
            getEventsTimer = new Timer() {
                @Override
                public void run() {
                    getEventsFromServer();
                }
            };
            getEventsTimer.scheduleRepeating(seconds * 1000);
        }
    }

    private void stopGetEventsTimer() {
        if (getEventsTimer == null) {
            return;
        }
        getEventsTimer.cancel();
        getEventsTimer = null;
    }

    /*
     * Get events from server methods
     */

    public void getEventsFromServer() {
        OntologyServiceManager.getInstance().getEvents(project.getProjectName(), revision, new GetEventsFromServerHandler());
    }

    public void dispose() {
        stopGetEventsTimer();
        ontologyListeners.clear(); //seems to be safe to get rid of the listeners
        events.clear();
        isTimerInitialized = false;
    }


    /*
     * Remote calls
     */

    class GetEventsFromServerHandler extends AbstractAsyncHandler<List<OntologyEvent>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at getting events from server", caught);
            errorsFromServer++;
            if (errorsFromServer > ERROR_REPORTING_THRESHOLD) {
                Window.alert("There are problems communicating with the server." + "Please restart the browser and try again.");
                stopGetEventsTimer();
                errorsFromServer = 0;
            }
        }

        @Override
        public void handleSuccess(List<OntologyEvent> serverEvents) {
            if (serverEvents == null || serverEvents.size() == 0) {
                return;
            }
            /*
             *  ServerEvents come as a list. The last one has the latest
             *  server revision.
             */
            //TODO: make the revision assignment before or after dispatch?
            revision = serverEvents.get(serverEvents.size() - 1).getRevision();
            System.out.println("Server revision: " + revision + " Events size: " + serverEvents.size() + " events: " + serverEvents);

            events.addAll(serverEvents);
            dispatchEvents(events);
        }
    }

}

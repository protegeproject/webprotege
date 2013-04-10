package edu.stanford.bmir.protege.web.server.watches;

import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/03/2013
 */
public class WatchNotificationGeneratorManager {

    private OWLAPIProject project;

    private List<WatchNotificationGenerator> generators = new ArrayList<WatchNotificationGenerator>();

    public WatchNotificationGeneratorManager(OWLAPIProject project) {
        this.project = project;
    }

    public void addNotificationGenerator(WatchNotificationGenerator generator) {
        generators.add(generator);
    }
}

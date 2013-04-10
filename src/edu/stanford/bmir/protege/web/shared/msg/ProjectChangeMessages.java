package edu.stanford.bmir.protege.web.shared.msg;

import com.google.gwt.i18n.client.Messages;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/02/2013
 */
public interface ProjectChangeMessages extends Messages {

//    @DefaultMessage("Created {0} as a subclass of {1}")
//    String createdClass(String subClassBrowserText, String superClassBrowserText);

    @DefaultMessage("Created {0, list} as subclasses of {1}")
    @AlternateMessage({"one", "Created {0} as a subclass of {1}"})
    String createdClasses(List<String> subclassBrowserText, String superClassBrowserText);

    @DefaultMessage("Deleted classses {0, list}")
    @AlternateMessage({"one", "Deleted class {0}"})
    String deletedClasses(List<String> browserText);


}

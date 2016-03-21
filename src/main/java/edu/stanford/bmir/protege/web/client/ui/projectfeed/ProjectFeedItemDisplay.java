package edu.stanford.bmir.protege.web.client.ui.projectfeed;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public interface ProjectFeedItemDisplay extends HasVisibility, IsWidget {

    void updateElapsedTimeDisplay();

    UserId getUserId();

}

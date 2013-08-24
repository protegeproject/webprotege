package edu.stanford.bmir.protege.web.client.banner;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.actionbar.application.ApplicationActionBar;
import edu.stanford.bmir.protege.web.client.actionbar.project.ProjectActionBar;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2013
 */
public interface BannerView extends IsWidget {

    ApplicationActionBar getApplicationActionBar();

    ProjectActionBar getProjectActionBar();

}

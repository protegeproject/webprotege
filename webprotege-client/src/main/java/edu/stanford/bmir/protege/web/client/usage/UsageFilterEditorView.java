package edu.stanford.bmir.protege.web.client.usage;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/07/2013
 */
public interface UsageFilterEditorView extends IsWidget {

    UsageFilter getUsageFilter();
}

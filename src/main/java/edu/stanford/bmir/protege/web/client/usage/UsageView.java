package edu.stanford.bmir.protege.web.client.usage;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.usage.UsageFilter;
import edu.stanford.bmir.protege.web.shared.usage.UsageReference;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/07/2013
 */
public interface UsageView extends IsWidget, HasValueChangeHandlers<UsageFilter> {

    UsageFilter getUsageFilter();

    void showFilter();

    void setData(OWLEntity subject, Collection<UsageReference> references);

    void clearData();

}

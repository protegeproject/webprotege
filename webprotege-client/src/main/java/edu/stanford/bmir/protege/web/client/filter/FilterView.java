package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public interface FilterView extends IsWidget, HasValueChangeHandlers<FilterSet> {

    void setFilterIdRenderer(FilterIdRenderer renderer);

    void addFilterGroup(String filterGroup);

    void addFilter(FilterId filterId, FilterSetting initialSetting);

    void closeCurrentGroup();

    FilterSet getFilterSet();


}

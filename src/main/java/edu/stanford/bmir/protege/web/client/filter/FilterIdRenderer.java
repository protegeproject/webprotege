package edu.stanford.bmir.protege.web.client.filter;

import edu.stanford.bmir.protege.web.shared.filter.FilterId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public interface FilterIdRenderer {

    String render(FilterId filterId);
}

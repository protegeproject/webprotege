package edu.stanford.bmir.protege.web.client.search;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-24
 */
public interface EntitySearchResultView extends IsWidget {

    void setEntity(@Nonnull EntityNode entity);

    void setResultMatchViews(@Nonnull ImmutableList<SearchResultMatchView> views);

    void clearOboId();

    void setOboId(@Nonnull String oboId);
}

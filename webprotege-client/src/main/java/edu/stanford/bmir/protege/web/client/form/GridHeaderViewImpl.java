package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-27
 */
public class GridHeaderViewImpl extends Composite implements GridHeaderView {

    interface GridHeaderViewImplUiBinder extends UiBinder<HTMLPanel, GridHeaderViewImpl> {

    }

    private static GridHeaderViewImplUiBinder ourUiBinder = GWT.create(GridHeaderViewImplUiBinder.class);

    @UiField
    HTMLPanel headerContainer;

    @Inject
    public GridHeaderViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addColumnHeader(@Nonnull IsWidget headerWidget) {
        checkNotNull(headerWidget);
        headerContainer.add(headerWidget);
    }

    @Override
    public void clear() {
        headerContainer.clear();
    }
}

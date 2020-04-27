package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-20
 */
public class FormStackViewImpl extends Composite implements FormStackView {


    interface FormStackViewImplUiBinder extends UiBinder<HTMLPanel, FormStackViewImpl> {

    }

    private static FormStackViewImplUiBinder ourUiBinder = GWT.create(FormStackViewImplUiBinder.class);


    @UiField
    FlowPanel container;

    @UiField
    SimplePanel selectorContainer;

    private LanguageMapCurrentLocaleMapper currentLocaleMapper = new LanguageMapCurrentLocaleMapper();

    @Inject
    public FormStackViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public FormContainer addContainer(@Nonnull LanguageMap labels) {
        FormStackContainer simplePanel = new FormStackContainer();
        container.add(simplePanel);
        return new FormContainer() {
            @Override
            public void setWidget(IsWidget w) {
                simplePanel.setWidget(w);
            }

            @Override
            public boolean isVisible() {
                return simplePanel.isVisible();
            }

            @Override
            public void setVisible(boolean visible) {
                simplePanel.setVisible(visible);
            }

            @Override
            public Widget asWidget() {
                return simplePanel;
            }
        };
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getSelectorContainer() {
        return selectorContainer;
    }

    @Override
    public void clear() {
        container.clear();
    }
}

package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-27
 */
public class FormTabViewImpl extends Composite implements FormTabView {

    public static final String SELECTED_ITEM_STYLE = WebProtegeClientBundle.BUNDLE.style()
                                                                                  .formTabBar__tab__selected();

    interface FormTabViewImplUiBinder extends UiBinder<HTMLPanel, FormTabViewImpl> {

    }

    private static FormTabViewImplUiBinder ourUiBinder = GWT.create(FormTabViewImplUiBinder.class);

    @UiField
    Label label;

    private HandlerRegistration handlerRegistration = () -> {};

    private final LanguageMapCurrentLocaleMapper localeMapper = new LanguageMapCurrentLocaleMapper();

    @Inject
    public FormTabViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        this.label.setText(localeMapper.getValueForCurrentLocale(label));
    }

    @Override
    public void setSelected(boolean selected) {
        if(selected) {
            addStyleName(SELECTED_ITEM_STYLE);
        }
        else {
            removeStyleName(SELECTED_ITEM_STYLE);
        }
    }

    @Override
    public void setClickHandler(@Nonnull ClickHandler clickHandler) {
        handlerRegistration.removeHandler();
        handlerRegistration = label.addClickHandler(clickHandler);
    }
}

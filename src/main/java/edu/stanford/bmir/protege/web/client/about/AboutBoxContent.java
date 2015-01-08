package edu.stanford.bmir.protege.web.client.about;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/08/2013
 */
public class AboutBoxContent extends Composite {

    interface AboutBoxContentUiBinder extends UiBinder<HTMLPanel, AboutBoxContent> {

    }

    private static AboutBoxContentUiBinder ourUiBinder = GWT.create(AboutBoxContentUiBinder.class);

    @UiField
    protected HTML html;


    public AboutBoxContent() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        html.setHTML(WebProtegeClientBundle.BUNDLE.aboutBoxText().getText());
    }
}
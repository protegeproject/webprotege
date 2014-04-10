package edu.stanford.bmir.protege.web.client.ui.verification;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/07/2013
 */
public class NullHumanVerificationWidget extends Composite implements HumanVerificationWidget {

    public NullHumanVerificationWidget() {
        initWidget(new SimplePanel());
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HumanVerificationServiceProvider getVerificationServiceProvider() {
        return new NullHumanVerificationServiceProvider();
    }
}

package edu.stanford.bmir.protege.web.client.ui.library.recaptcha;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.gwtext.client.widgets.MessageBox;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class RecaptchaWidget extends FlowPanel {


    public static final String RECAPTCHA_WIDGET_DIV_ID = "recaptcha-widget-div";

    public static final String RECAPTCHA_PUBLIC_KEY = "6Le8WNISAAAAAB_NG0dp-qoXv7PgfiB1wJJpmaZT";

    /**
     * Add the recaptcha widget to this widget.  This has to be done dynamically.
     */
    public native void doRecaptchaWidgetSetup()/*-{
        $wnd.initRecapture();
    }-*/;


    public RecaptchaWidget() {
        getElement().setId(RECAPTCHA_WIDGET_DIV_ID);
        // It seems like we need a slight delay for things to work - not sure why.
        Timer timer = new Timer() {
            @Override
            public void run() {
                try {
                    doRecaptchaWidgetSetup();
                }
                catch (RuntimeException e) {
                    e.printStackTrace();
                    MessageBox.alert(e.getMessage());
                }
            }
        };
        timer.schedule(500);
    }

    /**
     * Gets the response to the ReCaptcha challenge
     * @return The response
     */
    public native String getResponse()
    /*-{
        return $wnd.Recaptcha.get_response();
    }-*/;

    /**
     * Gets the ReCaptcha challenge.
     * @return The challenge.
     */
    public native String getChallenge()
    /*-{
        return $wnd.Recaptcha.get_challenge();
    }-*/;
}

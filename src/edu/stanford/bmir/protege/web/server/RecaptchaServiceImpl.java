package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.RecaptchaService;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/06/2012
 */
public class RecaptchaServiceImpl extends RemoteServiceServlet implements RecaptchaService {

    private static final String PRIVATE_KEY = "6Le8WNISAAAAAAOO9H7DqIfADsDTnppfbXUN9r1r";

    private static final String CAPTCHA_URL = "http://www.google.com/recaptcha/api/verify";

    public boolean isSuccessful(String challenge, String response) {
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(PRIVATE_KEY);

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(CAPTCHA_URL, challenge, response);
        if(reCaptchaResponse.isValid()) {
            return true;
        }
        else {
            return false;
        }
    }
}

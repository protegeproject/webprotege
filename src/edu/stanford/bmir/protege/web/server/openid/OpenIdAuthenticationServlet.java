/**
 * 
 */
package edu.stanford.bmir.protege.web.server.openid;

import edu.stanford.bmir.protege.web.client.ui.login.constants.AuthenticationConstants;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.sreg.SRegRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author z.khan
 * 
 */
public class OpenIdAuthenticationServlet extends HttpServlet {

    private static final long serialVersionUID = -4764648759333345074L;
    private static final Log log = LogFactory.getLog(OpenIdAuthenticationServlet.class);

    @Override
    @SuppressWarnings("unchecked")
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpResp) {
        String returnUrl = httpServletRequest.getRequestURL().toString();
        returnUrl = returnUrl.substring(0, returnUrl.lastIndexOf('/')) + "/openidresponse";
        String openId = httpServletRequest.getParameter("openId");
        String openIdProvider = httpServletRequest.getParameter(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);
        String domainNameAndPort = httpServletRequest.getParameter(AuthenticationConstants.DOMAIN_NAME_AND_PORT);
        String protocol = httpServletRequest.getParameter(AuthenticationConstants.PROTOCOL);
        if (domainNameAndPort != null) {
            URL url;
            try {
                url = new URL(returnUrl);
                returnUrl = returnUrl.replace(url.getProtocol() + ":", protocol);
                if (url.getPort() != -1) {
                    returnUrl = returnUrl.replace(url.getHost() + ":" + url.getPort(), domainNameAndPort);
                } else {
                    returnUrl = returnUrl.replace(url.getHost(), domainNameAndPort);
                }
            } catch (MalformedURLException e) {
                log.warn("Malformed URL for OpenId: " + returnUrl, e);
            }
        }

        try {
            ConsumerManager manager = new ConsumerManager();
            manager.setAssociations(new InMemoryConsumerAssociationStore());
            manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
            manager.getRealmVerifier().setEnforceRpId(false);
            // perform discovery on the user-supplied identifier

            List discoveries = manager.discover(openId);

            // attempt to associate with the OpenID provider
            // and retrieve one service endpoint for authentication
            DiscoveryInformation discovered = manager.associate(discoveries);

            HttpSession httpSession = httpServletRequest.getSession(true);
            httpSession.setAttribute("openid-disc", discovered);
            httpSession.setAttribute("discovered", discovered);
            httpSession.setAttribute("manager", manager);
            httpSession.setAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER, openIdProvider);

            // obtain a AuthRequest message to be sent to the OpenID provider
            AuthRequest authReq = manager.authenticate(discovered, returnUrl);

            FetchRequest fetch = FetchRequest.createFetchRequest();
            fetch.addAttribute("email" // attribute alias
                    , "http://schema.openid.net/contact/email" // type URI
                    , true); // required
            fetch.addAttribute("email", "http://axschema.org/contact/email", true);
            authReq.addExtension(fetch);

            SRegRequest sregReq = SRegRequest.createFetchRequest();
            sregReq.addAttribute("email", true);
            authReq.addExtension(sregReq);

            httpResp.sendRedirect(authReq.getDestinationUrl(true));

        } catch (Exception e) {
            log.error("Exception OpenIdAuthenticationServlet :" + e);
        }

    }
}

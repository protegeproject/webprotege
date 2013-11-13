package edu.stanford.bmir.protege.web.client.ui.portlet.html;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;

import java.util.Map;

/**
 * @author Csongor Nyulas
 */
public class HtmlTextComponent extends Composite {

    private Map<String, Object> configProperties;
    private String loadURL = null;
    private String htmlText = null;
    private RequestBuilder requestBuilder = null;

    private HTML html;

    public HtmlTextComponent(Project project) {
        initUI();
    }

    private void initUI() {
        html = new HTML();
        html.setSize("100%", "100%");
        initWidget(html);
        refreshContent();
    }

    public void setConfigProperties(Map<String, Object> configProperties) {
        this.configProperties = configProperties;
        refreshContent();
    }

    public void refreshContent() {
        readConfigurations();

        if (loadURL != null) {
            try {
                getURLRequester().send();
            } catch (RequestException e) {
                GWT.log("Sending HTML request via RequestBuilder failed", e);
            }
        } else if (htmlText != null) {
            replaceHTML(htmlText);
        }
    }

    private void replaceHTML(String html) {
        this.addStyleName("html-portlet");
        this.html.setHTML(html);
    }

    private void readConfigurations() {
        if (configProperties != null) {
            loadURL = (String) configProperties.get(FormConstants.LOAD_URL);
            htmlText = (String) configProperties.get(FormConstants.TEXT);
        }
    }

    private RequestBuilder getURLRequester() {
        if (requestBuilder == null) {
            requestBuilder = new RequestBuilder(RequestBuilder.GET, loadURL);
            requestBuilder.setCallback(new RequestCallback() {

                public void onResponseReceived(Request request, Response response) {
                    String responseHtmlText = response.getText();
                    replaceHTML(responseHtmlText);
                }

                public void onError(Request request, Throwable exception) {
                    String responseHtmlText = exception.getMessage();
                    replaceHTML(responseHtmlText);
                }
            });
        }
        return requestBuilder;
    }

}

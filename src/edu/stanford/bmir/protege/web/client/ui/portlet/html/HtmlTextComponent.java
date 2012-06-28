package edu.stanford.bmir.protege.web.client.ui.portlet.html;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AnchorLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;

/**
 * @author Csongor Nyulas
 */
public class HtmlTextComponent extends Panel {

    private Project project;
    private Map<String, Object> configProperties;
    private String loadURL = null;
    private String htmlText = null;
    private RequestBuilder requestBuilder = null;

    public HtmlTextComponent(Project project) {
        this.project = project;
        initUI();
    }

    private void initUI() {
        this.setLayout(new AnchorLayout());
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
        this.addClass("html-portlet");

        this.removeAll();
        this.add(new HTML(html));
        this.doLayout();
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

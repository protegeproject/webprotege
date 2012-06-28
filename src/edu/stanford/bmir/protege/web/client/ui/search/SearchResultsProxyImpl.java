package edu.stanford.bmir.protege.web.client.ui.search;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.UrlParam;

import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.ui.util.GWTProxy;

public class SearchResultsProxyImpl extends GWTProxy {

    private String projectName = null;

    private String searchText = null;

    private ValueType valueType = null;

    private AsyncCallback<Boolean> asyncCallback;

    public SearchResultsProxyImpl() {
        this(null);
    }

    public SearchResultsProxyImpl(AsyncCallback<Boolean> asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public void resetParams() {
        this.projectName = null;
        this.searchText = null;
        this.valueType = null;
    }

    @Override
    public void load(int start, int limit, String sort, String dir, final JavaScriptObject o, UrlParam[] baseParams) {

        OntologyServiceManager.getInstance().search(projectName, searchText, valueType, start, limit, sort, dir, new AsyncCallback<PaginationData<EntityData>>() {
            public void onFailure(Throwable caught) {
                GWT.log("Error at search", caught);

                loadResponse(o, false, 0, (JavaScriptObject) null);

                if (asyncCallback != null) {
                    asyncCallback.onFailure(caught);
                }
            }

            public void onSuccess(PaginationData<EntityData> result) {

                if (result == null) {
                    loadResponse(o, true, 0, (JavaScriptObject) null);
                }
                else {
                    loadResponse(o, true, result.getTotalRecords(), getRecords(result));
                }

                if (asyncCallback != null) {
                    asyncCallback.onSuccess((result != null && result.getTotalRecords() != 0) ? true : false);
                }
            }

            private Object[][] getRecords(PaginationData<EntityData> searchResults) {
                List<EntityData> results = searchResults.getData();
                Object[][] resultAsObjects = new Object[results.size()][1];

                int i = 0;
                for (EntityData ed : results) {
                    Object[] obj = getRow(ed);
                    resultAsObjects[i++] = obj;
                }
                return resultAsObjects;
            }

            private Object[] getRow(EntityData ed) {
                return new Object[]{ed, getHighlight(ed.getBrowserText(), searchText)};
            }
        });
    }

    private native String getHighlight(String text, String expr) /*-{
        var specials = new RegExp("[.*+?|()\\[\\]{}\\\\]", "g"); // .*+?|()[]{}\
        var keywords = expr.replace(specials, "\\$&").split(' ').join('|');
        var regex = new RegExp('(' + keywords + ')', 'gi');
        var result = text.replace(regex, "<b><span style='color:#224499; text-decoration:underline;'>$1</span></b>");
        return result;
    }-*/;
}

package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RenderingServiceAsync {

    void execute(GetRendering request, AsyncCallback<GetRenderingResponse> async);

}

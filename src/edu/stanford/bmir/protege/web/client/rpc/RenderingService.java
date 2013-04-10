package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/11/2012
 */
@RemoteServiceRelativePath("renderingservice")
public interface RenderingService extends RemoteService  {

    GetRenderingResponse execute(GetRendering command);
}

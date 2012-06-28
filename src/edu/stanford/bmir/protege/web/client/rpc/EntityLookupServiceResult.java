package edu.stanford.bmir.protege.web.client.rpc;

import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualEntity;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/05/2012
 */
public class EntityLookupServiceResult implements Serializable {

    private VisualEntity<?> visualEntity;
    
    private int browserTextMatchStart;
    
    private int browserTextMatchEnd;

    private EntityLookupServiceResult() {
    }

    public EntityLookupServiceResult(VisualEntity<?> visualEntity, int browserTextMatchStart, int browserTextMatchEnd) {
        this.visualEntity = visualEntity;
        this.browserTextMatchStart = browserTextMatchStart;
        this.browserTextMatchEnd = browserTextMatchEnd;
    }

    public VisualEntity<?> getVisualEntity() {
        return visualEntity;
    }

    public int getBrowserTextMatchStart() {
        return browserTextMatchStart;
    }

    public int getBrowserTextMatchEnd() {
        return browserTextMatchEnd;
    }
    
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        String browserText = visualEntity.getBrowserText();
        sb.append(browserText.substring(0, browserTextMatchStart));
        sb.append("<span class=\"web-protege-entity-match-substring\">");
        sb.append(browserText.substring(browserTextMatchStart, browserTextMatchEnd));
        sb.append("</span>");
        sb.append(browserText.substring(browserTextMatchEnd));
        return sb.toString();
    }
}

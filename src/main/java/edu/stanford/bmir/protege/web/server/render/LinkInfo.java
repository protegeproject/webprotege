package edu.stanford.bmir.protege.web.server.render;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 21/02/2014
 */
public class LinkInfo {

    private String linkAddress;

    private String linkContent;

    public LinkInfo(String linkAddress, String linkContent) {
        this.linkAddress = linkAddress;
        this.linkContent = linkContent;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public String getLinkContent() {
        return linkContent;
    }
}

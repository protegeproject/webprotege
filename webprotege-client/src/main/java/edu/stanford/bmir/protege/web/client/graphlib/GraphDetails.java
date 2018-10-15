package edu.stanford.bmir.protege.web.client.graphlib;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Oct 2018
 */
@JsType
public class GraphDetails {

    @JsProperty
    public native int getWidth();

    @JsProperty
    public native int getHeight();




    @JsProperty(name = "rankdir")
    private native void setRankDir(@Nonnull String rankdir);

    public final void setRankDirTopToBottom() {
        setRankDir("TB");
    }

    public final void setRankDirBottomToTop() {
        setRankDir("BT");
    }

    public final void setRankDirLeftToRight() {
        setRankDir("LR");
    }

    public final void setRankDirRightToLeft() {
        setRankDir("RL");
    }

    @JsProperty(name = "align")
    public native void setAlign(@Nonnull String align);

    @JsProperty(name = "nodesep")
    public native void setNodeSep(int nodesep);

    @JsProperty(name = "ranksep")
    public native void setRankSep(int ranksep);

    @JsProperty(name = "edgesep")
    public native void setEdgeSep(int edgesep);

    @JsProperty(name = "marginx")
    public native void setMarginX(int marginX);

    @JsProperty(name = "marginy")
    public native void setMarginY(int marginy);

    @JsProperty(name = "ranker")
    private native void setRanker(String ranker);

    public final void setRankerToNetworkSimplex() {
        setRanker("network-simplex");
    }

    public final void setRankerToTightTree() {
        setRanker("tight-tree");
    }

    public final void setRankerToLongestPath() {
        setRanker("longest-path");
    }
}

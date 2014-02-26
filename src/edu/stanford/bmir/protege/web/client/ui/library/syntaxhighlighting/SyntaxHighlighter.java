package edu.stanford.bmir.protege.web.client.ui.library.syntaxhighlighting;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/06/2012
 */
public class SyntaxHighlighter extends FlowPanel {

    private int counter = 0;

    private String id;



    public SyntaxHighlighter() {
        try {
            counter++;
            id = "cm-el-" + id;
//            id = "code-mirror-element";
            getElement().setId(id);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        setup(id);
    }

    private native void setup(String id)/*-{
        var element = $doc.getElementById(id);
        var myCodeMirror = $wnd.CodeMirror(element, {
            value: "hasTopping some MozzarellaTopping",
            mode:  "manchestersyntax",
            theme: "manchester-syntax"
        });
    }-*/;
}
